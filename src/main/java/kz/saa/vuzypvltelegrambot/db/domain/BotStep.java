package kz.saa.vuzypvltelegrambot.db.domain;

import kz.saa.vuzypvltelegrambot.model.Step;

import javax.persistence.*;

@Entity
public class BotStep {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;
    private Step step;
    private Integer sequence;
    private Long chatId;
    public BotStep() {
    }
    public BotStep(Step step, long chatId) {
        this.step = step;
        this.chatId = chatId;
    }
    public Long getId() {
        return id;
    }

    public Step getStep() {
        return step;
    }

    public void setStep(Step step) {
        this.step = step;
    }

    public Integer getSequence() {
        return sequence;
    }

    public void setSequence(Integer sequence) {
        this.sequence = sequence;
    }

    public Long getChatId() {
        return chatId;
    }

    public void setChatId(Long chatId) {
        this.chatId = chatId;
    }

    @Override
    public String toString() {
        return "BotStep{" +
                "id=" + id +
                ", step=" + step +
                ", sequence=" + sequence +
                ", chatId=" + chatId +
                '}';
    }
}

