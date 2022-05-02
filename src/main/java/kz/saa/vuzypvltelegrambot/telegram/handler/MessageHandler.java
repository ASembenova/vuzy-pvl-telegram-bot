package kz.saa.vuzypvltelegrambot.telegram.handler;


import com.vdurmont.emoji.EmojiParser;
import kz.saa.vuzypvltelegrambot.db.domain.User;
import kz.saa.vuzypvltelegrambot.model.Step;
import kz.saa.vuzypvltelegrambot.service.*;
import kz.saa.vuzypvltelegrambot.service.report.PassportService;
import kz.saa.vuzypvltelegrambot.service.report.CompareService;
import kz.saa.vuzypvltelegrambot.service.speciality.InfoService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;

@Component
public class MessageHandler {
    //private final UserCash userCash;
    private final LocaleService localeService;
    private final MessageSender messageSender;
    private final MenuService menuService;
    private final InfoService infoService;
    private final CompareService compareService;
    private final PassportService passportService;
    private final UserServiceDBImpl userService;


    public MessageHandler(/*UserCash userCash, */LocaleService localeService, MessageSender messageSender, MenuService menuService, InfoService infoService, CompareService compareService, PassportService passportService, UserServiceDBImpl userService) {
        /*this.userCash = userCash;*/
        this.localeService = localeService;
        this.messageSender = messageSender;
        this.menuService = menuService;
        this.infoService = infoService;
        this.compareService = compareService;
        this.passportService = passportService;
        this.userService = userService;
    }

    public BotApiMethod<?> handle(Message message) {
        String messageText = message.getText();
        long chatId = message.getChatId();
        Step newStep = null;
        Step currentStep = null;
        if(!userService.containsUser(chatId) || messageText.equals("/start")){
            userService.addUser(new User(chatId, message.getFrom().getFirstName(), message.getFrom().getLastName()));
            currentStep = userService.getLastStep(chatId).getStep();
            newStep = nextStep(currentStep, messageText, chatId);
        } else{
            if(!messageText.equals(localeService.getMessage("menu.back", chatId))) {
                currentStep = userService.getLastStep(chatId).getStep();
                if(messageText.equals(localeService.getMessage("menu.help", chatId))){
                    newStep = Step.HELP;
                } else{
                    newStep = nextStep(currentStep, messageText, chatId);
                }
            } else {
                currentStep = userService.deleteLastStep(chatId).getStep();
                newStep = userService.getLastStep(chatId).getStep();
            }
        }
        //System.out.println("current: "+currentStep+", new: "+newStep);
        userService.addStep(chatId, newStep);
        return processNewStep(newStep, chatId, message);
    }

    private BotApiMethod<?> processNewStep(Step step, long chatId, Message message) {
        String messageText = message.getText();
        String stepName = step.name();
        switch (stepName) {
            case "WELCOME":
                return menuService.getWelcomeMsg(chatId);
            case "MAIN_MENU":
                return menuService.getMainMenuMsg(chatId);
            case "SELECT_ALL":
                return menuService.getSelectAllMsg(chatId);
            case "SELECT_ONE":
                if (messageText.equals(localeService.getMessage("menu.one", chatId)) ||
                        messageText.equals(localeService.getMessage("menu.back", chatId))) {
                    return menuService.getSelectOneMsg(chatId);
                } else {
                    return infoService.getOneVuzInfo(chatId, messageText);
                }
            case "LANG_MENU":
                if (messageText.equals(localeService.getMessage("menu.change_lang", chatId))) {
                    return menuService.getSelectLangMsg(chatId);
                }
                if (messageText.equals(EmojiParser.parseToUnicode(localeService.getMessage("lang.kz", chatId)))) {
                    localeService.changeLang("kz", chatId);
                } else if (messageText.equals(EmojiParser.parseToUnicode(localeService.getMessage("lang.ru", chatId)))) {
                    localeService.changeLang("ru", chatId);
                }
                userService.deleteLastStep(chatId);
                return menuService.getMainMenuMsg(chatId);
            case "PARAMS_ALL":
                if (messageText.equals(localeService.getMessage("menu_all.all_param", chatId))) {
                    return menuService.getAllParamsMsg(chatId);
                } else if (messageText.equals(EmojiParser.parseToUnicode(localeService.getMessage("btn.generate_pdf", chatId)))) {
                    passportService.getAllParamsPassport(chatId, message.getFrom().getFirstName(), message.getFrom().getLastName());
                }
                return null;
            case "PARAMS_CUSTOM":
                return menuService.getChooseParamsMsg(chatId);
/*else if(messageText.equals(localeService.getMessage("menu_all.all_param", chatId))) {
                    return menuService.getAllParamsMsg(chatId);
                } else if(messageText.equals(localeService.getMessage("menu.search", chatId))){
                    return menuService.getSearchMsg(chatId);
                }*/
            case "COMPARE_SPEC":
                return menuService.getCompareSpecMsg(chatId);
            case "COMPARE_BYNAME":
            case "COMPARE_BYCODE":
            case "COMPARE_BYNAME_AND_CODE":
                return compareService.getCompareMessage(chatId, stepName.toLowerCase());
            case "SEARCH":
                if (messageText.equals(localeService.getMessage("menu.search", chatId))) {
                    return menuService.getSearchMsg(chatId);
                } else {
                    return menuService.getSearchResults(chatId, messageText);
                }
            case "HELP":
                if (messageText.equals(localeService.getMessage("menu.help", chatId))) {
                    return menuService.getHelpMessage(chatId);
                } else if (messageText.equals(localeService.getMessage("help.dataset_info", chatId))) {
                    return menuService.getHelpDatasetPassportMessage(chatId);
                } else if (messageText.equals(localeService.getMessage("help.opendata", chatId))) {
                    return menuService.getHelpOpenDataMessage(chatId);
                } else if (messageText.equals(localeService.getMessage("help.dataegov", chatId))) {
                    return menuService.getHelpDataEgovMessage(chatId);
                } else if (messageText.equals(localeService.getMessage("help.write", chatId))) {
                    return menuService.getHelpWriteMessage(chatId);
                } else messageSender.sendMsgToDeveloper(message);
        }
        return null;
    }

    private Step nextStep(Step currentStep, String messageText, long chatId) {
        String name = currentStep.name();
        switch (name){
            case "START":
                return Step.WELCOME;
            /*case "SELECT_LANG":*/
            case "WELCOME":
                if(messageText.equals(EmojiParser.parseToUnicode(localeService.getMessage("lang.kz", chatId)))){
                    localeService.changeLang("kz", chatId);
                } else {
                    localeService.changeLang("ru", chatId);
                }
                return Step.MAIN_MENU;
            case "MAIN_MENU":
                if(messageText.equals(localeService.getMessage("menu.all", chatId))){
                    return Step.SELECT_ALL;
                } else if(messageText.equals(localeService.getMessage("menu.one", chatId))){
                    return Step.SELECT_ONE;
                } else if(messageText.equals(localeService.getMessage("menu.change_lang", chatId))){
                    return Step.LANG_MENU;
                } else {
                    return Step.MAIN_MENU;
                }
            case "PARAMS_CUSTOM":
            case "SELECT_ALL":
                if(messageText.equals(localeService.getMessage("menu_all.all_param", chatId))){
                    return Step.PARAMS_ALL;
                } else if(messageText.equals(localeService.getMessage("menu_all.select_param", chatId))){
                    return Step.PARAMS_CUSTOM;
                } else if(messageText.equals(localeService.getMessage("menu_all.compare_number", chatId))){
                    return Step.COMPARE_NUMBER;
                } else if(messageText.equals(localeService.getMessage("menu_all.compare_spec", chatId))){
                    return Step.COMPARE_SPEC;
                } else if(messageText.equals(localeService.getMessage("menu.search", chatId))){
                    return Step.SEARCH;
                } else {
                    return Step.SELECT_ALL;
                }
            case "COMPARE_SPEC":
            case "COMPARE_BYNAME":
            case "COMPARE_BYCODE":
            case "COMPARE_BYNAME_AND_CODE":
                if(messageText.equals(localeService.getMessage("menu.compare_byname", chatId))){
                    return Step.COMPARE_BYNAME;
                } else if(messageText.equals(localeService.getMessage("menu.compare_bycode", chatId))){
                    return Step.COMPARE_BYCODE;
                } else if(messageText.equals(localeService.getMessage("menu.compare_byname_and_code", chatId))){
                    return Step.COMPARE_BYNAME_AND_CODE;
                } else {
                    return Step.SELECT_ALL;
                }
        }
        return currentStep;
    }


}
