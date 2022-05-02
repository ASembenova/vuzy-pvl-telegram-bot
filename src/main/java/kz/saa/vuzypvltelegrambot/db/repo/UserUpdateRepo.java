package kz.saa.vuzypvltelegrambot.db.repo;

import kz.saa.vuzypvltelegrambot.db.domain.UserUpdate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserUpdateRepo extends JpaRepository<UserUpdate, Long> {

    List<UserUpdate> findAllByChatId(long chatId);
}
