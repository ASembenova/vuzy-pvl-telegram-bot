package kz.saa.vuzypvltelegrambot.service.report;

import kz.saa.vuzypvltelegrambot.db.domain.GeneratedDocument;
import kz.saa.vuzypvltelegrambot.db.repo.GeneratedDocumentRepo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DocumentDBService {
    private final GeneratedDocumentRepo generatedDocumentRepo;

    public DocumentDBService(GeneratedDocumentRepo generatedDocumentRepo) {
        this.generatedDocumentRepo = generatedDocumentRepo;
    }

    public void add(GeneratedDocument generatedDocument){
        generatedDocumentRepo.save(generatedDocument);
    }

    public List<GeneratedDocument> findAll(){
        return generatedDocumentRepo.findAll();
    }

    public int getCounter(){
        return findAll().size();
    }

}

