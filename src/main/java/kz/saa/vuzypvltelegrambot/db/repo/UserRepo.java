package kz.saa.vuzypvltelegrambot.db.repo;

import kz.saa.vuzypvltelegrambot.db.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepo extends JpaRepository<User, Long> {
    User findByChatId(long chatId);
}