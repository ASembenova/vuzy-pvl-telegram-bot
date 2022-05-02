package kz.saa.vuzypvltelegrambot.service;

import org.springframework.stereotype.Service;

@Service
public interface LocaleService {
    void changeLang(String localeTag, long chatId);
    String getMessage(String tag, long chatId);
    String getLocaleTag(long chatId);
    boolean isEmpty();
    boolean containsUser(long chatId);
}
