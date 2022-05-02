package kz.saa.vuzypvltelegrambot.service.speciality;

import kz.saa.vuzypvltelegrambot.egovapi.DataObjectService;
import kz.saa.vuzypvltelegrambot.egovapi.Vuz;
import kz.saa.vuzypvltelegrambot.model.Degree;
import kz.saa.vuzypvltelegrambot.model.Speciality;
import kz.saa.vuzypvltelegrambot.service.LocaleService;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class SpecialityService {
    private final LocaleService localeService;
    private final DataObjectService dataObjectService;
    private final SplitterService splitterService;

    public SpecialityService(LocaleService localeService, DataObjectService dataObjectService, SplitterService splitterService) {
        this.localeService = localeService;
        this.dataObjectService = dataObjectService;
        this.splitterService = splitterService;
    }

    public String searchByKeyword(String keyword, long chatId){
        Vuz[] vuzy = dataObjectService.getVuzy();
        int counter = 0;
        StringBuilder result = new StringBuilder();
        for (Vuz v:vuzy) {
            if(localeService.getLocaleTag(chatId).equals("kz")){
                result.append("<b>").append(splitterService.splitFullname(v.name1)).append("</b>");
            } else{
                result.append("<b>").append(splitterService.splitFullname(v.name2)).append("</b>");
            }
            result.append("\n");
            StringBuilder temp = new StringBuilder();
            List<String> list = getSimpleListByRegex(v, chatId);
            for (String spec:list) {
                if (spec.toLowerCase().contains(keyword.toLowerCase())){
                    temp.append(spec);
                    temp.append("\n");
                    counter++;
                }
            }
            if (temp.toString().isEmpty()){
                temp.append("<i>").append(localeService.getMessage("search.not_found", chatId)).append("</i>").append("\n");
            }
            result.append(temp.toString());
            result.append("\n");
        }
        result.append("<b><i>").append(localeService.getMessage("total", chatId));
        result.append(": ").append(counter).append("</i></b>");
        return result.toString();
    }

    public List<String> getSimpleListByRegex(Vuz vuz, long chatId){
        List<String> list = new ArrayList<>();
        String data;
        if(localeService.getLocaleTag(chatId).equals("kz")){
            data = vuz.name6;
        } else{
            data = vuz.name7;
        }
        data = data.replaceAll(", 6", ",\n6");
        data = data.replaceAll(",6", ",\n6");
        data = data.replaceAll(",  6", ",\n6");
        data = data.replaceAll(", 5", ",\n5");
        data = data.replaceAll(", 7", ",\n7");
        data = data.replaceAll(", 8", ",\n8");
        String regex = "([5-8])([BDMВМ])(\\d{5,6})(.*)([,]?)";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(data);
        while (matcher.find()) {
            String found = data.substring(matcher.start(), matcher.end());
            if ((found.charAt(found.length()-1))==','){
                found = found.substring(0, found.length()-1).trim();
            }
            list.add(found);
        }
        return list;
    }

    private Set<Speciality> getSpecialitySet(Vuz vuz, Comparator<Speciality> comparator, String lang){
        Set<Speciality> set = new TreeSet<>(comparator);
        String data;
        if(lang.equals("ru")){
            data = vuz.name7;
        } else {
            data = vuz.name6;
        }
        data = data.replaceAll(", 6", ",\n6");
        data = data.replaceAll(",6", ",\n6");
        data = data.replaceAll(",  6", ",\n6");
        data = data.replaceAll(", 5", ",\n5");
        data = data.replaceAll(", 7", ",\n7");
        data = data.replaceAll(", 8", ",\n8");
        String regex = "([5-8])([BDMВМ])(\\d{5,6})([\\s-]+)([^\\n]*)([,]*)";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(data);
        while (matcher.find()) {
            Speciality spec = new Speciality();
            spec.setVuzFullname(splitterService.splitFullname(vuz.name2));
            spec.setCode(matcher.group(1)+matcher.group(2)+matcher.group(3));
            //spec.setCode(matcher.group(3));
            String name = matcher.group(5).trim();
            if(name.charAt(name.length()-1)==','){
                name = name.substring(0, name.length()-1);
            }
            spec.setName(name);
            switch (matcher.group(2)){
                case "B":
                case "В":
                    spec.setDegree(Degree.B);
                    break;
                case "M":
                case "М":
                    spec.setDegree(Degree.M);
                    break;
                case "D":
                    spec.setDegree(Degree.D);
                    break;
            }
            set.add(spec);
        }

        return set;

    }

    public List<Set<Speciality>> getIntersectionByName(Vuz v1, Vuz v2, String lang){
        Comparator<Speciality> byName = Comparator.comparing(Speciality::getDegree).thenComparing(Speciality::getCodeSize).thenComparing(Speciality::getCodeFirstSymbol).thenComparing(Speciality::getMeaningNumber).thenComparing(Speciality::getName);
        return getIntersection(v1, v2, byName, lang);
    }

    public List<Set<Speciality>> getIntersectionByCode(Vuz v1, Vuz v2, String lang){
        Comparator<Speciality> byCode = Comparator.comparing(Speciality::getDegree).thenComparing(Speciality::getCode);
        return getIntersection(v1, v2, byCode, lang);
    }

    public List<Set<Speciality>> getIntersectionByNameAndCode(Vuz v1, Vuz v2, String lang){
        Comparator<Speciality> byNameAndCode = Comparator.comparing(Speciality::getDegree).thenComparing(Speciality::getCode).thenComparing(Speciality::getName);
        return getIntersection(v1, v2, byNameAndCode, lang);
    }

    public List<Set<Speciality>> getDifferenceByName(Vuz v1, Vuz v2, String lang){
        Comparator<Speciality> byName = Comparator.comparing(Speciality::getDegree).thenComparing(Speciality::getCodeSize).thenComparing(Speciality::getCodeFirstSymbol).thenComparing(Speciality::getMeaningNumber).thenComparing(Speciality::getName);
        return getDifference(v1, v2, byName, lang);
    }

    public List<Set<Speciality>> getDifferenceByCode(Vuz v1, Vuz v2, String lang){
        Comparator<Speciality> byCode = Comparator.comparing(Speciality::getDegree).thenComparing(Speciality::getCode);
        return getDifference(v1, v2, byCode, lang);
    }

    public List<Set<Speciality>> getDifferenceByNameAndCode(Vuz v1, Vuz v2, String lang){
        Comparator<Speciality> byNameAndCode = Comparator.comparing(Speciality::getDegree).thenComparing(Speciality::getCode).thenComparing(Speciality::getName);
        return getDifference(v1, v2, byNameAndCode, lang);
    }

    private List<Set<Speciality>> getIntersection(Vuz v1, Vuz v2, Comparator<Speciality> comparator, String lang){
        Set<Speciality> set1 = getSpecialitySet(v1, comparator, lang);
        Set<Speciality> set2 = getSpecialitySet(v2, comparator, lang);
        Set<Speciality> setCommon1 = new TreeSet<>(comparator);
        setCommon1.addAll(set1);
        setCommon1.retainAll(set2);
        Set<Speciality> setCommon2 = new TreeSet<>(comparator);
        setCommon2.addAll(set2);
        setCommon2.retainAll(set1);
        List<Set<Speciality>> commons = new ArrayList<>();
        commons.add(setCommon1);
        commons.add(setCommon2);
        return commons;
    }

    private List<Set<Speciality>> getDifference(Vuz v1, Vuz v2, Comparator<Speciality> comparator, String lang){
        Set<Speciality> set1 = getSpecialitySet(v1,comparator, lang);
        List<Set<Speciality>> intersections = getIntersection(v1, v2, comparator, lang);
        set1.removeAll(intersections.get(0));
        Set<Speciality> set2 = getSpecialitySet(v2,comparator, lang);
        set2.removeAll(intersections.get(1));
        List<Set<Speciality>> difference = new ArrayList<>();
        difference.add(set1);
        difference.add(set2);
        return difference;
    }
}
