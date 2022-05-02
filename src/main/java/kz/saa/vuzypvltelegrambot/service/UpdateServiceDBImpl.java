package kz.saa.vuzypvltelegrambot.service;

import kz.saa.vuzypvltelegrambot.db.domain.UserUpdate;
import kz.saa.vuzypvltelegrambot.db.repo.UserUpdateRepo;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

@Component
public class UpdateServiceDBImpl implements UpdateService{
    private final UserUpdateRepo userUpdateRepo;

    public UpdateServiceDBImpl(UserUpdateRepo userUpdateRepo) {
        this.userUpdateRepo = userUpdateRepo;
    }

    @Override
    public void save(Update update) {
        userUpdateRepo.save(new UserUpdate(update));
    }

    @Override
    public List<UserUpdate> findAll() {
        return userUpdateRepo.findAll();
    }

    @Override
    public List<UserUpdate> findAllByChatId(long chatId) {
        return userUpdateRepo.findAllByChatId(chatId);
    }
}
