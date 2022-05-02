package kz.saa.vuzypvltelegrambot.service.speciality;
import com.vdurmont.emoji.EmojiParser;
import kz.saa.vuzypvltelegrambot.egovapi.DataObjectService;
import kz.saa.vuzypvltelegrambot.egovapi.Vuz;
import kz.saa.vuzypvltelegrambot.service.LocaleService;
import kz.saa.vuzypvltelegrambot.service.MessageSender;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendContact;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendVenue;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class InfoService {
    private final DataObjectService dataObjectService;
    private final LocaleService localeService;
    private final MessageSender messageSender;
    private final SpecialityService specialityService;
    private final SplitterService splitterService;
    private final List<List<String>> list = new ArrayList<>();

    public InfoService(DataObjectService dataObjectService, LocaleService localeService, MessageSender messageSender, SpecialityService specialityService, SplitterService splitterService) {
        this.dataObjectService = dataObjectService;
        this.localeService = localeService;
        this.messageSender = messageSender;
        this.specialityService = specialityService;
        this.splitterService = splitterService;
        list.add(Arrays.asList("btn.previous", "btn.next"));
        list.add(Arrays.asList("btn.speclist"));
        list.add(Arrays.asList("btn.geo"));
        list.add(Arrays.asList("btn.call"));
        list.add(Arrays.asList("btn.site"));
    }

    public SendMessage getOneVuzInfo(long chatId, String vuzName){
        int index = dataObjectService.getIndexByVuzName(vuzName, chatId);
        return getOneVuzInfo(chatId, index);
    }

    private SendMessage getOneVuzInfo(long chatId, int index){
        String textMessage = dataObjectService.oneVuzInfo(index, chatId);
        final InlineKeyboardMarkup inlineKeyboardMarkup = getInlineMessageButtons(index, chatId);
        return messageSender.createMessageWithInlineKeyboard(chatId, EmojiParser.parseToUnicode(textMessage), inlineKeyboardMarkup);
    }

    private InlineKeyboardMarkup getInlineMessageButtons(int index, long chatId) {
        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
        for (List<String> listTag:list) {
            List<InlineKeyboardButton> row = new ArrayList<>();
            for (String tag:listTag) {
                InlineKeyboardButton btn = new InlineKeyboardButton();
                btn.setText(EmojiParser.parseToUnicode(localeService.getMessage(tag, chatId)));
                btn.setCallbackData(tag+index);
                if(tag.equals("btn.site")){
                    btn.setUrl(dataObjectService.getSiteUrl(index));
                }
                row.add(btn);
            }
            rowList.add(row);
        }
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        inlineKeyboardMarkup.setKeyboard(rowList);
        return inlineKeyboardMarkup;
    }
    public EditMessageText getEditedMessageAboutVuz(long chatId, long messageId, int newIndex){
        EditMessageText newMessage = new EditMessageText();
        newMessage.setMessageId((int) messageId);
        newMessage.setText(EmojiParser.parseToUnicode(dataObjectService.oneVuzInfo(newIndex, chatId)));
        newMessage.setChatId(String.valueOf(chatId));
        newMessage.setReplyMarkup((InlineKeyboardMarkup) getOneVuzInfo(chatId, newIndex).getReplyMarkup());
        newMessage.setParseMode("html");
        return newMessage;
    }

    public EditMessageText getMessageWithNextVuz(long chatId, long messageId, int index){
        int newIndex = index+1;
        if(newIndex>=dataObjectService.getVuzy().length){
            newIndex=0;
        }
        return getEditedMessageAboutVuz(chatId, messageId, newIndex);
    }

    public EditMessageText getMessageWithPreviousVuz(long chatId, long messageId, int index){
        int newIndex = index-1;
        if(newIndex<0){
            newIndex = dataObjectService.getVuzy().length-1;
        }
        return getEditedMessageAboutVuz(chatId, messageId, newIndex);
    }

    public SendMessage getMessageWithSpecList(long chatId, int index) {
        Vuz vuz = dataObjectService.getVuzy()[index];
        int count = specialityService.getSimpleListByRegex(vuz, chatId).size();
        String data;
        String vuzName;
        if (localeService.getLocaleTag(chatId).equals("kz")) {
            data = vuz.name6;
            vuzName = splitterService.splitFullname(vuz.name1);
        } else {
            data = vuz.name7;
            vuzName = splitterService.splitFullname(vuz.name2);
        }
        data = data.replaceAll(", 6", ",\n6");
        data = data.replaceAll(",6", ",\n6");
        data = data.replaceAll(",  6", ",\n6");
        data = data.replaceAll(", 5", ",\n5");
        data = data.replaceAll(", 7", ",\n7");
        data = data.replaceAll(", 8", ",\n8");
        StringBuilder result = new StringBuilder();
        result.append("<b>").append(vuzName).append("</b>").append("\n\n");
        result.append("<i>").append(EmojiParser.parseToUnicode(localeService.getMessage("spec_list", chatId))).append("</i>").append("\n\n");
        result.append(data).append("\n\n");
        result.append("<b>").append(localeService.getMessage("total", chatId)).append(": "+count).append("</b>");
        return messageSender.createMessageWithKeyboard(chatId, result.toString(), null);
    }

    public SendVenue getMessageWithLocation(long chatId, int index){
        Vuz vuz = dataObjectService.getVuzy()[index];
        String locationStr = vuz.name18;
        String address;
        String title;
        if (localeService.getLocaleTag(chatId).equals("kz")){
            address = vuz.name9 + ", " + vuz.name16;
            title = splitterService.splitFullname(vuz.name1);
        } else {
            address = vuz.name10 +", " + vuz.name17;
            title = splitterService.splitFullname(vuz.name2);
        }
        double[] location = splitterService.splitGeo(locationStr);
        return new SendVenue(String.valueOf(chatId), location[0], location[1], title, address); //return new SendLocation(String.valueOf(chatId), latitude, longtitude);
    }

    public SendContact getMessageWithContact(long chatId, int index) {
        String phone = splitterService.splitPhone(dataObjectService.getVuzy()[index].name11);
        String name = splitterService.splitFullname(dataObjectService.getVuzy()[index].name2);
        return new SendContact(String.valueOf(chatId),phone,name);
    }


}

