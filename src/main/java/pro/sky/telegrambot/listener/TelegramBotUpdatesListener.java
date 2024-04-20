package pro.sky.telegrambot.listener;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.DeleteMyCommands;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.GetUpdates;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
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
    private boolean startIsPressed = false;
    private boolean tableLoaded = false;

    private Logger logger = LoggerFactory.getLogger(TelegramBotUpdatesListener.class);

    private final TelegramBotService telegramBotService;

    public TelegramBotUpdatesListener(TelegramBotService telegramBotService) {
        this.telegramBotService = telegramBotService;
    }

    //    final TelegramBot telegramBot = new TelegramBot(token);

//    final UpdatesListener updatesListener = new UpdatesListener() {
//        @Override
//        public int process(List<Update> updates) {
//            updates.forEach(update -> {
//                long chatId = update.message().chat().id();
//                logger.info("Processing update: {}", chatId);
//
//                SendMessage message = new SendMessage(chatId, "Merhaba abi!");
//                SendResponse response = telegramBot.execute(message);
//
//                String text = update.message().text();
//                if(text != null && text.equals("/start")) {
//                    telegramBot.execute(new SendMessage(chatId, "Merhaba abi! Ne haber?"));
//                }
//            });
//            return UpdatesListener.CONFIRMED_UPDATES_ALL;
//        }
//    };

    @PostConstruct
    public void init() {
        TelegramBot telegramBot = new TelegramBot(token);
        telegramBot.execute(new DeleteMyCommands());
        telegramBotService.clearTable();

        logger.info("telegramBot created");
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
                    tableLoaded = true;
                }
                if(text != null && text.equals("/clear") && tableLoaded) {
                    telegramBotService.clearTable();
                    tableLoaded = false;
                }
                if(text != null && text.equals("/print")) {
                    telegramBotService.printTable(telegramBot, chatId);
                    tableLoaded = false;
                }

            });
            return UpdatesListener.CONFIRMED_UPDATES_ALL;

        });
    }

//    @Override
//    public int process(List<Update> updates) {
//        updates.forEach(update -> {
//            long chatId = update.message().chat().id();
//            logger.info("Processing update: {}", chatId);
//
//            String text = update.message().text();
//            if(text != null && text.equals("/start")) {
//                telegramBot.execute(new SendMessage(chatId, "Merhaba abi! Ne haber?"));
//            }
//
//
//
//        });
//        return UpdatesListener.CONFIRMED_UPDATES_ALL;


}
