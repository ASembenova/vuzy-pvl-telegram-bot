package kz.saa.vuzypvltelegrambot.db.domain;


import com.vdurmont.emoji.EmojiParser;

import javax.persistence.*;

@Entity
@Table(name="telegram_update")
public class UserUpdate {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;

    private Long chatId;
    private String text;
    private String callbackData;

    public UserUpdate() {
    }

    public UserUpdate(org.telegram.telegrambots.meta.api.objects.Update update) {
        if(update.hasMessage()){
            text = EmojiParser.parseFromUnicode(update.getMessage().getText(), new EmojiParser.EmojiTransformer() {
                @Override
                public String transform(EmojiParser.UnicodeCandidate unicodeCandidate) {
                    return "";
                }
            });
            chatId = update.getMessage().getChatId();
        }
        if(update.hasCallbackQuery()){
            callbackData = update.getCallbackQuery().getData();
            chatId = update.getCallbackQuery().getFrom().getId();
        }
    }

    @Override
    public String toString() {
        return "Update{" +
                "id=" + id +
                ", chatId=" + chatId +
                ", text='" + text + '\'' +
                ", callbackData='" + callbackData + '\'' +
                '}';
    }

    public Long getChatId() {
        return chatId;
    }

    public void setChatId(Long chatId) {
        this.chatId = chatId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getCallbackData() {
        return callbackData;
    }

    public void setCallbackData(String callbackData) {
        this.callbackData = callbackData;
    }
}
