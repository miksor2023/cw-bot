package pro.sky.telegrambot.listener;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;

import com.pengrad.telegrambot.request.SendMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import pro.sky.telegrambot.service.TelegramBotService;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

@Service
public class TelegramBotUpdatesListener {

    @Value("${telegram.bot.token}")
    private String token;
    @Autowired
    private TelegramBot telegramBot;

    private boolean startIsPressed = false;//флаг - была получена команда /start, нужен для логики ожидания ответа
    private boolean tableLoaded = false;//флаг - таблица загружена - для отладки использовал, решил пока не удалять
    private boolean sendDialogStarted = false;//флаг - получена команда /send , нужен для логики ожидания ответа

    private Logger logger = LoggerFactory.getLogger(TelegramBotUpdatesListener.class);



    private final TelegramBotService telegramBotService;

    public TelegramBotUpdatesListener(TelegramBotService telegramBotService) {
        this.telegramBotService = telegramBotService;
    }

    @PostConstruct
    public void init() {
        telegramBot.setUpdatesListener(updates -> {
            updates.forEach(update -> {
                long chatId = update.message().chat().id();
                logger.info("init() invoked, chat_id = " + chatId);
                String text = update.message().text();

                if(sendDialogStarted) {
                    List<Long> ids = telegramBotService.getIdList();
                    List<Long> enteredIds = new ArrayList<>();
                    Scanner scanner = new Scanner(text);
                    while(scanner.hasNextLong()){
                        Long enteredId = scanner.nextLong();
                        if(ids.contains(enteredId) && !enteredIds.contains(enteredId)) {
                            enteredIds.add(enteredId);
                        }
                    }
                    if(!enteredIds.isEmpty()) {
                        telegramBot.execute(new SendMessage(chatId, "ids of entries to send:\n " + enteredIds.toString()));
                        sendDialogStarted = false;
                        telegramBotService.sendChosenTasks(enteredIds);
                    } else {
                        telegramBot.execute(new SendMessage(chatId, "Sorry, entered ids don't match"));
                    }
                }
                if(text != null && text.equals("/start")) {
                    telegramBot.execute(new SendMessage(chatId, "Merhaba abi! Nasılsın?"));
                    startIsPressed = true;
                }
                if(text != null && text.equalsIgnoreCase("her şey yolunda") && startIsPressed){
                    telegramBotService.sayHallo(chatId);
                    startIsPressed = false;
                }
                if(text != null && text.equals("/load") && !tableLoaded) {
                    telegramBotService.loadTestTable(chatId);
                    tableLoaded = true;
                }
                if(text != null && text.equals("/clear") && tableLoaded) {
                    telegramBotService.clearTable(chatId);
                    tableLoaded = false;
                }
                if(text != null && text.equals("/print")) {
                    telegramBotService.printTable(chatId);
                }
                if(text != null && text.equals("/send")) {
                    telegramBotService.printTable(chatId);
                    telegramBot.execute(new SendMessage(chatId, "Type entry's ids to send, delimited by spase"));
                    sendDialogStarted = true;
                }

                if(text != null && telegramBotService.textMaches(text)) {
                    telegramBotService.putEntry(chatId, text);
                }
            });
            return UpdatesListener.CONFIRMED_UPDATES_ALL;

        });
    }
}
