package kz.saa.vuzypvltelegrambot.db.domain;


import kz.saa.vuzypvltelegrambot.model.Step;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class User {
    @Id
    private Long chatId;
    private String firstname;
    private String lastname;
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "chatId")
    private List<BotStep> stepList;

    /*@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "chatId")
    private List<Update> updateList;*/

    public User() {
    }

    public User(long chatId, String firstname, String lastname) {
        this.chatId = chatId;
        this.firstname = firstname;
        this.lastname = lastname;
        this.stepList = new ArrayList<>();
        stepList.add(new BotStep(Step.START, chatId));
    }


    public Long getChatId() {
        return chatId;
    }

    public void setChatId(Long chatId) {
        this.chatId = chatId;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public List<BotStep> getStepList() {
        return stepList;
    }

    public BotStep getLastStep(){
        return stepList.get(stepList.size()-1);
    }

    public void setStepList(List<BotStep> stepList) {
        this.stepList = stepList;
    }

    public BotStep removeLastStep(){
        List<BotStep> stepList = getStepList();
        return stepList.remove(stepList.size()-1);
    }

    @Override
    public String toString() {
        return "User{" +
                "chatId=" + chatId +
                ", firstname='" + firstname + '\'' +
                ", lastname='" + lastname + '\'' +
                ", stepList=" + stepList +
                '}';
    }

    /*public List<Update> getUpdateList() {
        return updateList;
    }

    public void setUpdateList(List<Update> updateList) {
        this.updateList = updateList;
    }*/
}
