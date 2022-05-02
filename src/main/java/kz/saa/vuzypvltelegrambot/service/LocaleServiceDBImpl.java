package kz.saa.vuzypvltelegrambot.service;

import kz.saa.vuzypvltelegrambot.db.domain.Lang;
import kz.saa.vuzypvltelegrambot.db.repo.LangRepo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.util.Locale;

@Service
public class LocaleServiceDBImpl implements LocaleService {

    private final LangRepo langRepo;
    private Locale defaultLocale;
    private MessageSource messageSource;

    public LocaleServiceDBImpl(LangRepo langRepo, @Value("${localeTag}") String localeTag, MessageSource messageSource) {
        this.langRepo = langRepo;
        this.messageSource = messageSource;
        this.defaultLocale = Locale.forLanguageTag(localeTag);
    }


    @Override
    public void changeLang(String localeTag, long chatId) {
        Lang lang = new Lang();
        lang.setLocale(Locale.forLanguageTag(localeTag));
        lang.setChatId(chatId);
        langRepo.save(lang);
    }

    @Override
    public String getMessage(String tag, long chatId) {
        if (!containsUser(chatId)){
            Lang lang = new Lang(chatId, defaultLocale);
            langRepo.save(lang);
        }
        Locale locale = langRepo.findByChatId(chatId).getLocale();
        return messageSource.getMessage(tag, null, locale);
    }

    @Override
    public String getLocaleTag(long chatId) {
        if (!containsUser(chatId)){
            Lang lang = new Lang(chatId, defaultLocale);
            langRepo.save(lang);
        }
        return langRepo.findByChatId(chatId).getLocale().getLanguage();
    }

    @Override
    public boolean isEmpty() {
        return langRepo.findAll().isEmpty();
    }

    @Override
    public boolean containsUser(long chatId) {
        return langRepo.findByChatId(chatId)!=null;
    }
}
