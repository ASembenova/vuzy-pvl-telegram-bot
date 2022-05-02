package kz.saa.vuzypvltelegrambot.service.speciality;

import org.springframework.stereotype.Service;

@Service
public class SplitterService {

    public String splitFullname(String s){
        return s.substring(s.indexOf("«") + 1, s.indexOf("»"));
    }

    public double[] splitGeo(String original){
        double[] location = new double[2];
        String[] locationStr = original.split(", ");
        location[0] = Double.parseDouble(locationStr[0]);
        location[1] = Double.parseDouble(locationStr[1]);
        return location;
    }

    public String splitPhone(String original){
        if(original.contains(",")){
            return original.substring(0, original.indexOf(","));
        } else return original;
    }
}
