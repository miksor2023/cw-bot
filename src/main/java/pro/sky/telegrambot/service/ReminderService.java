package pro.sky.telegrambot.service;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.SendMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import pro.sky.telegrambot.listener.TelegramBotUpdatesListener;
@Service
public class ReminderService {
    private final TelegramBot telegramBot;

    public ReminderService(TelegramBot telegramBot) {
        this.telegramBot = telegramBot;
    }

    private Logger logger = LoggerFactory.getLogger(ReminderService.class);

//    @Scheduled(cron = "${interval-in-cron}")
    @Scheduled(cron = "0 * * * * *")
    public void sendReminder(){
        logger.info("sendReminder was invoked");
        telegramBot.execute(new SendMessage(278265466L, "Merhaba abi! Nasılsın?"));
    }
}
