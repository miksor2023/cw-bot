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
import java.util.List;

@Service
public class TelegramBotUpdatesListener {

    @Value("${telegram.bot.token}")
    private String token;
    @Autowired
    private TelegramBot telegramBot;
    private boolean startIsPressed = false;
    private boolean tableLoaded = false;

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
                if(text != null && text.equals("/start")) {
                    telegramBot.execute(new SendMessage(chatId, "Merhaba abi! Nasılsın?"));
                    startIsPressed = true;
                }
                if(text != null && text.equalsIgnoreCase("her şey yolunda") && startIsPressed){
                    telegramBotService.sayHallo(telegramBot, chatId);
//                    telegramBot.execute(new SendMessage(chatId, "Maşallah! Maşallah!"));
                    startIsPressed = false;
                }
                if(text != null && text.equals("/load") && !tableLoaded) {
                    telegramBotService.loadTestTable();
                    telegramBot.execute(new SendMessage(chatId, "Table loaded"));
                    tableLoaded = true;
                }
                if(text != null && text.equals("/clear") && tableLoaded) {
                    telegramBotService.clearTable();
                    telegramBot.execute(new SendMessage(chatId, "Table cleared"));
                    tableLoaded = false;
                }
                if(text != null && text.equals("/print")) {
                    telegramBotService.printTable(telegramBot, chatId);
                    tableLoaded = false;
                }
                if(text != null && telegramBotService.textMaches(text)) {
                    telegramBotService.putEntry(chatId, text);
                    telegramBot.execute(new SendMessage(chatId, "Entry added"));
                }

            });
            return UpdatesListener.CONFIRMED_UPDATES_ALL;

        });
    }
}
