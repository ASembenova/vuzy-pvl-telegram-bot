package kz.saa.vuzypvltelegrambot.db.repo;

import kz.saa.vuzypvltelegrambot.db.domain.Lang;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface LangRepo extends JpaRepository<Lang, Long> {

    Lang findByChatId(long chatId);
    List<Lang> findAll();

}
