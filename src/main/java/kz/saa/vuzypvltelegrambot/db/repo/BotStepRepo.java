package kz.saa.vuzypvltelegrambot.db.repo;

import kz.saa.vuzypvltelegrambot.db.domain.BotStep;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BotStepRepo extends JpaRepository<BotStep, Long> {
    List<BotStep> findAllByChatId(long chatId);
}