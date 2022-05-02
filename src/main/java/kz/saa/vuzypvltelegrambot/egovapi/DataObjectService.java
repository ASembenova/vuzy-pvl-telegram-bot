package kz.saa.vuzypvltelegrambot.egovapi;

import kz.saa.vuzypvltelegrambot.service.LocaleService;
import kz.saa.vuzypvltelegrambot.service.speciality.SplitterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class DataObjectService {
    private Vuz[] vuzy;
    private Metadata metadata;
    private Field[] vuzFields;
    private Method[] metaMethods;
    private boolean[] flagsRu;
    private boolean[] flagsKz;
    private String[][] datasetPassport;
    private final LocaleService localeService;
    private final SplitterService splitterService;
    private Map<Integer, String> metaRu;
    private Map<Integer, String> metaKz;
    @Autowired
    public DataObjectService(EgovApiConnection egovApiConnection, LocaleService localeService, SplitterService splitterService){
        vuzy = egovApiConnection.createVuzObjects();
        datasetPassport = egovApiConnection.getPassport();
        metadata = egovApiConnection.createMetadataObject();
        initFlags();
        this.localeService = localeService;
        this.splitterService = splitterService;
    }

    private void initFlags(){
        metaRu = new LinkedHashMap<>();
        metaKz = new LinkedHashMap<>();
        vuzFields = vuzy[0].getClass().getDeclaredFields();
        metaMethods = getMetadata().getClass().getDeclaredMethods();
        flagsRu = new boolean[metaMethods.length];
        flagsKz = new boolean[metaMethods.length];
        try {
            for (int i = 0; i < metaMethods.length; i++) {
                String s = metaMethods[i].getName();
                Pattern p = Pattern.compile("(\\D+)(\\d{1,2})(\\D+)"); // example: getName1Kk
                Matcher m = p.matcher(s);
                while (m.find()){
                    int num = Integer.parseInt(m.group(2));
                    if(m.group(3).equals("Kk")){
                        metaKz.put(num, metaMethods[i].invoke(metadata).toString());
                        flagsKz[i]=true;
                    } else if (m.group(3).equals("Ru")){
                        metaRu.put(num, metaMethods[i].invoke(metadata).toString());
                        flagsRu[i]=true;
                    } else{
                        flagsKz[i]=true;
                        flagsRu[i]=true;
                    }
                }
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    public Vuz[] getVuzy() {
        return vuzy;
    }

    public Metadata getMetadata(){
        return metadata;
    }

    public String[][] getDatasetPassport() { return datasetPassport; }

    public Map<Integer, String> getMetaRu() {
        return metaRu;
    }

    public Map<Integer, String> getMetaKz() {
        return metaKz;
    }

    public Field[] getVuzFields() {
        return vuzFields;
    }

    public int getIndexByVuzName(String vuzname, long chatId){
        int index = 0;
        String[] vuzFullnames = new String[vuzy.length];
        if(localeService.getLocaleTag(chatId)=="ru"){
            for (int i = 0; i < vuzy.length; i++) {
                vuzFullnames[i] = vuzy[i].name2;
            }
        } else if(localeService.getLocaleTag(chatId)=="kz"){
            for (int i = 0; i < vuzy.length; i++) {
                vuzFullnames[i] = vuzy[i].name1;
            }
        }
        for (int i = 0; i < vuzy.length; i++) {
            if(vuzFullnames[i].contains(vuzname)){
                index = i;
                break;
            }
        }
        return index;
    }

    public String oneVuzInfo(int index, long chatId){
        if(localeService.getLocaleTag(chatId).equals("kz")){
            return oneVuzInfoKz(index);
        } else {
            return oneVuzInfoRu(index);
        }
    }

    private String oneVuzInfoRu(int index) {
        StringBuilder message = new StringBuilder();
        message.append("<b>").append(metadata.fields.name2.labelRu).append("</b>:\n"); //name rus
        message.append(vuzy[index].name2).append("\n\n");
        message.append(metadata.fields.name5.labelRu).append(":\n"); //direction
        message.append(vuzy[index].name5).append("\n\n");
        message.append(metadata.fields.name3.labelRu).append(" :male_office_worker: :\n"); //fio ruk
        message.append(vuzy[index].name3).append("\n\n");
        message.append(metadata.fields.name8.labelRu).append(" :busts_in_silhouette: :\n"); //number
        message.append(vuzy[index].name8).append("\n\n");
        message.append(metadata.fields.name15.labelRu).append(" :globe_with_meridians: :\n"); //web-site
        message.append(vuzy[index].name15).append("\n\n");
        message.append(metadata.fields.name17.labelRu).append(" :office: :\n"); //address
        message.append(vuzy[index].name10).append(", ").append(vuzy[index].name17).append("\n\n");
        message.append(metadata.fields.name11.labelRu).append(" :telephone_receiver::\n"); //phone
        message.append(vuzy[index].name11).append("\n\n");
        message.append(metadata.fields.name12.labelRu).append(" :e-mail: :\n"); //email
        message.append(vuzy[index].name12).append("\n\n");
        message.append(metadata.fields.name14.labelRu).append(" :hourglass_flowing_sand: :\n"); //working mode
        message.append(vuzy[index].name14).append("\n\n");
        return message.toString();
    }

    private String oneVuzInfoKz(int index) {
        StringBuilder message = new StringBuilder();
        message.append("<b>").append(metadata.fields.name1.labelKk).append("</b>:\n"); //name rus
        message.append(vuzy[index].name1).append("\n\n");
        message.append(metadata.fields.name4.labelKk).append(":\n"); //direction
        message.append(vuzy[index].name4).append("\n\n");
        message.append(metadata.fields.name3.labelKk).append(" :male_office_worker: :\n"); //fio ruk
        message.append(vuzy[index].name3).append("\n\n");
        message.append(metadata.fields.name8.labelKk).append(" :busts_in_silhouette: :\n"); //number
        message.append(vuzy[index].name8).append("\n\n");
        message.append(metadata.fields.name15.labelKk).append(":globe_with_meridians::\n"); //web-site
        message.append(vuzy[index].name15).append("\n\n");
        message.append(metadata.fields.name16.labelKk).append(":office::\n"); //address
        message.append(vuzy[index].name9).append(", ").append(vuzy[index].name16).append("\n\n");
        message.append(metadata.fields.name11.labelKk).append(" :telephone_receiver::\n"); //phone
        message.append(vuzy[index].name11).append("\n\n");
        message.append(metadata.fields.name12.labelKk).append(":e-mail::\n"); //email
        message.append(vuzy[index].name12).append("\n\n");
        message.append(metadata.fields.name13.labelKk).append(":hourglass_flowing_sand::\n"); //working mode
        message.append(vuzy[index].name13).append("\n\n");
        return message.toString();
    }

    public List<String> vuzList(long chatId){
        List<String> list = new ArrayList<>();
        if(localeService.getLocaleTag(chatId).equals("kz")){
            for (Vuz v:vuzy) {
                list.add(splitterService.splitFullname(v.name1));
            }
        } else {
            for (Vuz v:vuzy) {
                list.add(splitterService.splitFullname(v.name2));
            }
        }
        return list;
    }

    public String getSiteUrl(int index){
        return vuzy[index].name15;
    }

    public String getAllParamsInfo(long chatId) throws IllegalAccessException, InvocationTargetException {
        boolean[] flags;
        Map<Integer, String> meta = new HashMap<>();
        if(localeService.getLocaleTag(chatId).equals("ru")){
            flags = flagsRu;
            meta.putAll(metaRu);
        } else{
            flags = flagsKz;
            meta.putAll(metaKz);
        }
        int x = 0;
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < flags.length; i++) {
            Field field;
            if(flags[i]==true){
                if(x!=5 && x!=6){
                    field = vuzFields[x];
                    stringBuilder.append("<b>").append(meta.get(x+1)).append("</b>\n");
                    for (int j = 0; j < vuzy.length; j++) {
                        String s = field.get(vuzy[j]).toString();
                        stringBuilder.append((j+1)+" - "+s+"\n");
                    }
                    stringBuilder.append("\n");
                }
                x++;
            }
        }
        return stringBuilder.toString();
    }


}


