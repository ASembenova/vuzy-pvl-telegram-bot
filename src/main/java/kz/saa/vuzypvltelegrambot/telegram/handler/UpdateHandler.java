package kz.saa.vuzypvltelegrambot.telegram.handler;


import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.HashMap;
import java.util.Map;

@Component
public class UpdateHandler {
    private final CallbackQueryHandler callbackQueryHandler;
    private final MessageHandler messageHandler;
    public Map<String, Integer> updateCounter;

    public UpdateHandler(CallbackQueryHandler callbackQueryHandler, MessageHandler messageHandler) {
        this.callbackQueryHandler = callbackQueryHandler;
        this.messageHandler = messageHandler;
        updateCounter = new HashMap<>();
    }

    public BotApiMethod<?> handleUpdate(Update update) {
        if (update.hasMessage()){
            /*if(!updateCounter.containsKey(update.getMessage().getFrom().getFirstName())){
                updateCounter.put(update.getMessage().getFrom().getFirstName(), 0);
            }
            int temp = updateCounter.get(update.getMessage().getFrom().getFirstName());
            updateCounter.put(update.getMessage().getFrom().getFirstName(), temp+1);*/
            Message message = update.getMessage();
            if(message.hasText()){
                return messageHandler.handle(message);
            }
        } else if(update.hasCallbackQuery()){
            CallbackQuery callbackQuery = update.getCallbackQuery();
            return callbackQueryHandler.handle(callbackQuery);
        }
        return null;
    }

}

