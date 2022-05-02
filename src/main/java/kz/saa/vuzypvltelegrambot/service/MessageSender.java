package kz.saa.vuzypvltelegrambot.service;
import com.vdurmont.emoji.EmojiParser;
import kz.saa.vuzypvltelegrambot.db.domain.GeneratedDocument;
import kz.saa.vuzypvltelegrambot.service.report.DocumentDBService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Service
public class MessageSender {
    @Autowired
    @Lazy
    private AbsSender bot;
    private final LocaleService localeService;
    private final DocumentDBService documentDBService;

    public MessageSender(LocaleService localeService, DocumentDBService documentDBService) {
        this.localeService = localeService;
        this.documentDBService = documentDBService;
    }

    private SendMessage createMessageWithKeyboard(long chatId, String replyText, ReplyKeyboardMarkup replyKeyboardMarkup) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(chatId));
        sendMessage.setText(replyText);
        sendMessage.setParseMode("html");
        sendMessage.setReplyMarkup(replyKeyboardMarkup);
        return sendMessage;
    }

    public SendMessage createMessageWithKeyboard(long chatId, String replyText, List<String> list) {
        ReplyKeyboardMarkup replyKeyboardMarkup = null;
        if (list!=null){
            replyKeyboardMarkup = getMenuKeyboard(list);
        }
        return createMessageWithKeyboard(chatId, replyText, replyKeyboardMarkup);
    }

    public SendMessage createMessageWithKeyboardByTags(long chatId, String textTag, List<String> tags) {
        ReplyKeyboardMarkup replyKeyboardMarkup = null;
        String text = localeService.getMessage(textTag, chatId);
        if (tags!=null){
            replyKeyboardMarkup = getMenuKeyboard(tags, chatId);
        }
        return createMessageWithKeyboard(chatId, text, replyKeyboardMarkup);
    }

    public SendMessage createMessageWithInlineKeyboard(final long chatId, String textMessage, final InlineKeyboardMarkup inlineKeyboardMarkup) {
        final SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(true);
        sendMessage.setChatId(String.valueOf(chatId));
        sendMessage.setText(textMessage);
        sendMessage.setParseMode("html");
        if (inlineKeyboardMarkup != null) {
            sendMessage.setReplyMarkup(inlineKeyboardMarkup);
        }
        return sendMessage;
    }

    public SendMessage createMessageWithInlineKeyboard(final long chatId, String textMessage, List<String> namesOfButtons, List<String> fieldsList) {
        InlineKeyboardMarkup inlineKeyboardMarkup = null;
        if (namesOfButtons != null && fieldsList!=null) {
            inlineKeyboardMarkup = getInlineKeyboard(namesOfButtons, fieldsList);
        }
        return createMessageWithInlineKeyboard(chatId, textMessage, inlineKeyboardMarkup);
    }

    public InlineKeyboardMarkup getInlineKeyboard(List<String> list, List<String> fieldsList){
        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            List<InlineKeyboardButton> row = new ArrayList<>();
            InlineKeyboardButton btn = new InlineKeyboardButton();
            btn.setText(EmojiParser.parseToUnicode(list.get(i)));
            btn.setCallbackData(fieldsList.get(i));
            row.add(btn);
            rowList.add(row);
        }
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        inlineKeyboardMarkup.setKeyboard(rowList);
        return inlineKeyboardMarkup;
    }

    private ReplyKeyboardMarkup getMenuKeyboard(List<String> namesOfButtons) {
        final ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);
        List<KeyboardRow> keyboard = new ArrayList<>();
        for (String name: namesOfButtons) {
            KeyboardRow row = new KeyboardRow();
            row.add(new KeyboardButton(EmojiParser.parseToUnicode(name)));
            keyboard.add(row);
        }
        replyKeyboardMarkup.setKeyboard(keyboard);
        return replyKeyboardMarkup;
    }

    private ReplyKeyboardMarkup getMenuKeyboard(List<String> tags, long chatId) {
        final ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);
        List<KeyboardRow> keyboard = new ArrayList<>();
        for (String tag: tags) {
            KeyboardRow row = new KeyboardRow();
            row.add(new KeyboardButton(EmojiParser.parseToUnicode(localeService.getMessage(tag, chatId))));
            keyboard.add(row);
        }
        replyKeyboardMarkup.setKeyboard(keyboard);
        return replyKeyboardMarkup;
    }

    public void sendPdf(long chatId, File file, String firstname, String lastname){
        String fileId;
        SendDocument sendDocument = new SendDocument();
        sendDocument.setDocument(new InputFile(file));
        sendDocument.setChatId(String.valueOf(chatId));
        try {
            fileId = bot.execute(sendDocument).getDocument().getFileId();
            GeneratedDocument generatedDocument = new GeneratedDocument();
            generatedDocument.setCustomer(firstname+" "+lastname);
            generatedDocument.setFileId(fileId);
            generatedDocument.setDate(new java.sql.Date(System.currentTimeMillis()));
            generatedDocument.setTitle("test");
            documentDBService.add(generatedDocument);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public List<String> getButtonList(List<String> tags, long chatId){
        List<String> list = new ArrayList<>();
        for(String tag:tags){
            list.add(EmojiParser.parseToUnicode(localeService.getMessage(tag, chatId)));
        }
        return list;
    }

    public AnswerCallbackQuery getAnswerCallbackQuery(long chatId, String messageTag, String callbackQueryId){
        AnswerCallbackQuery answerCallbackQuery = new AnswerCallbackQuery();
        answerCallbackQuery.setText(localeService.getMessage(messageTag, chatId));
        answerCallbackQuery.setCallbackQueryId(callbackQueryId);
        return answerCallbackQuery;
    }

    public void execute(BotApiMethod botApiMethod){
        try {
            this.bot.execute(botApiMethod);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public void sendMsgToDeveloper(Message message){
        long chatId = message.getChatId();
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId("5244146363");
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(localeService.getMessage("help.write.title", chatId));
        stringBuilder.append(" ").append(message.getFrom().getFirstName());
        if(message.getFrom().getLastName()!=null){
            stringBuilder.append(" ").append(message.getFrom().getLastName());
        }
        stringBuilder.append("\n").append(message.getText());
        sendMessage.setText(stringBuilder.toString());
        try {
            this.bot.execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
