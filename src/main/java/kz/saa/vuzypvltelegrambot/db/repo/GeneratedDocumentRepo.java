package kz.saa.vuzypvltelegrambot.db.repo;

import kz.saa.vuzypvltelegrambot.db.domain.GeneratedDocument;
import kz.saa.vuzypvltelegrambot.db.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GeneratedDocumentRepo extends JpaRepository<GeneratedDocument, Long> {
    List<GeneratedDocument> findAllByCustomer(User customer);
}
