package kz.saa.vuzypvltelegrambot.service;

import kz.saa.vuzypvltelegrambot.db.domain.BotStep;
import kz.saa.vuzypvltelegrambot.db.repo.BotStepRepo;
import kz.saa.vuzypvltelegrambot.db.domain.User;
import kz.saa.vuzypvltelegrambot.db.repo.UserRepo;
import kz.saa.vuzypvltelegrambot.model.Step;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserServiceDBImpl implements UserService{

    private final UserRepo userRepo;
    private final BotStepRepo botStepRepo;

    public UserServiceDBImpl(UserRepo userRepo, BotStepRepo botStepRepo) {
        this.userRepo = userRepo;
        this.botStepRepo = botStepRepo;
    }

    @Override
    public void addUser(User user) {
        User old = userRepo.findByChatId(user.getChatId());
        if(old!=null){
            userRepo.delete(old);
        }
        userRepo.save(user);
    }

    @Override
    public void addStep(long chatId, Step step) {
        User user = userRepo.findByChatId(chatId);
        BotStep oldStep = user.getLastStep();
        BotStep newStep = new BotStep(step, chatId);
        newStep.setSequence(user.getStepList().size());
        if(!oldStep.getStep().name().equals(newStep.getStep().name())){
            user.getStepList().add(newStep);
            userRepo.save(user);
        }
    }

    @Override
    public BotStep deleteLastStep(long chatId) {
        User user = userRepo.getById(chatId);
        BotStep botStep = user.removeLastStep();
        userRepo.save(user);
        return botStep;
    }

    @Override
    public BotStep getLastStep(long chatId) {
        if(containsUser(chatId)){
            List<BotStep> list = userRepo.findByChatId(chatId).getStepList();
            return list.get(list.size()-1);
        }
        return null;
    }

    @Override
    public boolean containsUser(long chatId) {
        return userRepo.findByChatId(chatId)!=null;
    }

    @Override
    public List<User> findAll() {
        return userRepo.findAll();
    }
}

