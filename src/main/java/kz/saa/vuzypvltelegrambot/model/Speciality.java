package kz.saa.vuzypvltelegrambot.model;


public class Speciality{
    private String vuzFullname;
    private String code;
    private Degree degree;
    private String name;

    public String getVuzFullname() {
        return vuzFullname;
    }

    public void setVuzFullname(String vuzFullname) {
        this.vuzFullname = vuzFullname;
    }

    public String getCode() {
        return code;
    }

    public int getCodeSize() {
        return code.length();
    }

    public char getCodeFirstSymbol() {
        return code.charAt(0);
    }

    public int getMeaningNumber(){
        return Integer.parseInt(code.substring(2,4));
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Degree getDegree() {
        return degree;
    }

    public void setDegree(Degree degree) {
        this.degree = degree;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "\n"+code + ' ' + name+"\n";
    }



}

