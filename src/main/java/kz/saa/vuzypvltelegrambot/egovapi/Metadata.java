package kz.saa.vuzypvltelegrambot.egovapi;

import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.Date;

class Responsible{
    public String phone;
    public String email;
    public String fullnameEn;
    public String fullnameKk;
    public String fullnameRu;
}

class Owner{
    public String fullnameEn;
    public String fullnameKk;
    public String fullnameRu;
    public String identifier;
}

class Name15{
    public String labelKk;
    public String labelEn;
    public int orderNumber;
    public String type;
    public String labelRu;
}

class Name14{
    public String labelKk;
    public String labelEn;
    public int orderNumber;
    public String type;
    public String labelRu;
}

class Name13{
    public String labelKk;
    public String labelEn;
    public int orderNumber;
    public String type;
    public String labelRu;
}

class Name12{
    public String labelKk;
    public String labelEn;
    public int orderNumber;
    public String type;
    public String labelRu;
}

class Name11{
    public String labelKk;
    public String labelEn;
    public int orderNumber;
    public String type;
    public String labelRu;
}

class Name10{
    public String labelKk;
    public String labelEn;
    public int orderNumber;
    public String type;
    public String labelRu;
}
class Id{
    public String labelKk;
    public String labelEn;
    public int orderNumber;
    public String type;
    public String labelRu;
}

class Name5{
    public String labelKk;
    public String labelEn;
    public int orderNumber;
    public String type;
    public String labelRu;
}

class Name6{
    public String labelKk;
    public String labelEn;
    public int orderNumber;
    public String type;
    public String labelRu;
}

class Name3{
    public String labelKk;
    public String labelEn;
    public int orderNumber;
    public String type;
    public String labelRu;
}

class Name4{
    public String labelKk;
    public String labelEn;
    public int orderNumber;
    public String type;
    public String labelRu;
}

class Name9{
    public String labelKk;
    public String labelEn;
    public int orderNumber;
    public String type;
    public String labelRu;
}

class Name7{
    public String labelKk;
    public String labelEn;
    public int orderNumber;
    public String type;
    public String labelRu;
}

class Name8{
    public String labelKk;
    public String labelEn;
    public int orderNumber;
    public String type;
    public String labelRu;
}

class Name18{
    public String labelKk;
    public String labelEn;
    public int orderNumber;
    public String type;
    public String labelRu;
}

class Name1{
    public String labelKk;
    public String labelEn;
    public int orderNumber;
    public String type;
    public String labelRu;
}

class Name2{
    public String labelKk;
    public String labelEn;
    public int orderNumber;
    public String type;
    public String labelRu;
}

class Name16{
    public String labelKk;
    public String labelEn;
    public int orderNumber;
    public String type;
    public String labelRu;
}

class Name17{
    public String labelKk;
    public String labelEn;
    public int orderNumber;
    public String type;
    public String labelRu;
}

class Fields {
    public Name15 name15;
    public Name14 name14;
    public Name13 name13;
    public Name12 name12;
    public Name11 name11;
    public Name10 name10;
    public Id id;
    public Name5 name5;
    public Name6 name6;
    public Name3 name3;
    public Name4 name4;
    public Name9 name9;
    public Name7 name7;
    public Name8 name8;
    public Name18 name18;
    public Name1 name1;
    public Name2 name2;
    public Name16 name16;
    public Name17 name17;

}

@Component
public class Metadata{
    public String phone;
    public String descriptionKk;
    public ArrayList<String> keywords;
    public Responsible responsible;
    public String descriptionEn;
    public String nameEn;
    public String fullnameKk;
    public String nameKk;
    public Date modifiedDate;
    public String descriptionRu;
    public String nameRu;
    public String email;
    public Owner owner;
    public String identifier;
    public String fullnameRu;
    public Fields fields;
    public String changeLog;
    public String apiUri;

    public String getName1Ru(){
        return fields.name1.labelRu;
    }
    public String getName2Ru(){
        return fields.name2.labelRu;
    }
    public String getName3Ru(){
        return fields.name3.labelRu;
    }
    public String getName4Ru(){
        return fields.name4.labelRu;
    }
    public String getName5Ru(){
        return fields.name5.labelRu;
    }
    public String getName6Ru(){
        return fields.name1.labelRu;
    }
    public String getName7Ru(){
        return fields.name7.labelRu;
    }
    public String getName8Ru(){
        return fields.name8.labelRu;
    }
    public String getName9Ru(){
        return fields.name9.labelRu;
    }
    public String getName10Ru(){
        return fields.name10.labelRu;
    }
    public String getName11Ru(){
        return fields.name11.labelRu;
    }
    public String getName12Ru(){
        return fields.name12.labelRu;
    }
    public String getName13Ru(){
        return fields.name13.labelRu;
    }
    public String getName14Ru(){
        return fields.name14.labelRu;
    }
    public String getName15Ru(){
        return fields.name15.labelRu;
    }
    public String getName16Ru(){
        return fields.name16.labelRu;
    }
    public String getName17Ru(){
        return fields.name17.labelRu;
    }
    public String getName18Ru() { return fields.name18.labelRu;}

    public String getName1Kk(){
        return fields.name1.labelKk;
    }
    public String getName2Kk(){
        return fields.name2.labelKk;
    }
    public String getName3Kk(){
        return fields.name3.labelKk;
    }
    public String getName4Kk(){
        return fields.name4.labelKk;
    }
    public String getName5Kk(){
        return fields.name5.labelKk;
    }
    public String getName6Kk(){
        return fields.name1.labelKk;
    }
    public String getName7Kk(){
        return fields.name7.labelKk;
    }
    public String getName8Kk(){
        return fields.name8.labelKk;
    }
    public String getName9Kk(){
        return fields.name9.labelKk;
    }
    public String getName10Kk(){
        return fields.name10.labelKk;
    }
    public String getName11Kk(){
        return fields.name11.labelKk;
    }
    public String getName12Kk(){
        return fields.name12.labelKk;
    }
    public String getName13Kk(){
        return fields.name13.labelKk;
    }
    public String getName14Kk(){
        return fields.name14.labelKk;
    }
    public String getName15Kk(){
        return fields.name15.labelKk;
    }
    public String getName16Kk(){
        return fields.name16.labelKk;
    }
    public String getName17Kk(){
        return fields.name17.labelKk;
    }
    public String getName18Kk() { return fields.name18.labelRu; }
}




