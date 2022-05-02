package kz.saa.vuzypvltelegrambot.service.report;

import kz.saa.vuzypvltelegrambot.egovapi.DataObjectService;
import kz.saa.vuzypvltelegrambot.egovapi.Metadata;
import kz.saa.vuzypvltelegrambot.egovapi.Vuz;
import kz.saa.vuzypvltelegrambot.service.LocaleService;
import kz.saa.vuzypvltelegrambot.service.MessageSender;
import org.springframework.stereotype.Service;

import java.io.File;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class PassportService {
    private final PDFService pdfService;
    private final MessageSender messageSender;
    private final LocaleService localeService;
    private final DataObjectService dataObjectService;
    private final DocumentDBService documentDBService;
    private final Metadata metadata;
    SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public PassportService(PDFService pdfService, MessageSender messageSender, LocaleService localeService, DataObjectService dataObjectService, DocumentDBService documentDBService, Metadata metadata) {
        this.pdfService = pdfService;
        this.messageSender = messageSender;
        this.localeService = localeService;
        this.dataObjectService = dataObjectService;
        this.documentDBService = documentDBService;
        this.metadata = metadata;
    }

    public Map<String, Object> getPassportVariables(Vuz[] vuzy, long chatId, boolean[] paramsFlag){
        int paramsCount = 0;
        for (boolean flag: paramsFlag) {
            if(flag==true){
                paramsCount++;
            }
        }
        String[][] array = new String[paramsFlag.length][vuzy.length+1];
        Field[] fields = dataObjectService.getVuzFields();
        Map<Integer, String> meta;
        if(localeService.getLocaleTag(chatId).equals("kz")){
            meta = dataObjectService.getMetaKz();
        } else{
            meta = dataObjectService.getMetaRu();
        }
        for (int i = 0; i < paramsFlag.length; i++) {
            for (int j = 0; j < vuzy.length; j++) {
                array[i][0] = meta.get(i+1);
                if(paramsFlag[i]==false){
                    break;
                } else {
                    try {
                        array[i][j+1] = fields[i].get(vuzy[j]).toString();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        Map<String, Object> variables = new HashMap<>();
        variables.put("array", array);
        variables.put("counter", String.format("%06d", documentDBService.getCounter()));
        variables.put("numOfVuz", vuzy.length);
        variables.put("datetime", formater.format(new Date(System.currentTimeMillis())));
        return variables;
    }

    public File getAllParamsPassport(long chatId, String firstname, String lastname){
        boolean[] b = new boolean[18];
        for (int i = 0; i < b.length; i++) {
            b[i] = true;
        }
        Map<String, Object> variables = this.getPassportVariables(dataObjectService.getVuzy(), chatId, b);
        variables.put("firstname", firstname);
        variables.put("lastname", lastname);
        try {
            File file = pdfService.generatePDF(variables, "params_"+ localeService.getLocaleTag(chatId));
            messageSender.sendPdf(chatId, file, firstname, lastname);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


}


