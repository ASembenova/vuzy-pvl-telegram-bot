package kz.saa.vuzypvltelegrambot.service;

import kz.saa.vuzypvltelegrambot.db.domain.BotStep;
import kz.saa.vuzypvltelegrambot.db.domain.User;
import kz.saa.vuzypvltelegrambot.model.Step;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface UserService {
    void addUser(User user);
    void addStep(long chatId, Step step);
    BotStep deleteLastStep(long chatId);
    BotStep getLastStep(long chatId);
    boolean containsUser(long chatId);
    List<User> findAll();
}
