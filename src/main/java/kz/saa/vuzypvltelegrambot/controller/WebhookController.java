package kz.saa.vuzypvltelegrambot.controller;


import kz.saa.vuzypvltelegrambot.service.LocaleService;
import kz.saa.vuzypvltelegrambot.service.UpdateService;
import kz.saa.vuzypvltelegrambot.service.UserService;
import kz.saa.vuzypvltelegrambot.service.report.DocumentDBService;
import kz.saa.vuzypvltelegrambot.telegram.VuzyPvlBot;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

@RestController
public class WebhookController {
    private final VuzyPvlBot bot;
    private final DocumentDBService documentDBService;
    private final UpdateService updateService;
    private final LocaleService localeService;
    private final UserService userService;


    public WebhookController(VuzyPvlBot bot, DocumentDBService documentDBService, UpdateService updateService, LocaleService localeService, UserService userService) {
        this.bot = bot;
        this.documentDBService = documentDBService;
        this.updateService = updateService;
        this.localeService = localeService;
        this.userService = userService;
    }

    @PostMapping("/")
    public BotApiMethod<?> onUpdateReceived(@RequestBody Update update) {
        updateService.save(update);
        return bot.onWebhookUpdateReceived(update);
    }

    @GetMapping("/users")
    public String getUsers(){
        StringBuilder stringBuilder = new StringBuilder("{");
        /*for (Long chatId : userCash.getUserMap().keySet()) {
            mapAsString.append(chatId + "=" + userCash.getUserMap().get(chatId));
        }*/
        stringBuilder.append(userService.findAll());
        return stringBuilder.append("}").toString();
    }

    @GetMapping("/updates")
    public String getUpdates(){
        StringBuilder mapAsString = new StringBuilder("[");
        mapAsString.append(updateService.findAll());
        return mapAsString.append("]").toString();
    }

    @GetMapping("/docs")
    public String getDocs(){
        StringBuilder mapAsString = new StringBuilder("[");
        for (int i = 0; i < documentDBService.findAll().size(); i++) {
            mapAsString.append(documentDBService.findAll().get(i));
        }
        return mapAsString.append("]").toString();
    }
}