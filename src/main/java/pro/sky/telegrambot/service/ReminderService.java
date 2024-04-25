package pro.sky.telegrambot.service;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.SendMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.support.CronExpression;
import org.springframework.stereotype.Service;
import pro.sky.telegrambot.entity.NotificationTask;
import pro.sky.telegrambot.listener.TelegramBotUpdatesListener;
import pro.sky.telegrambot.repository.NotificationTaskRepository;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;


public class ReminderService {
    private TelegramBot telegramBot;
    private final NotificationTaskRepository notificationTaskRepository;
    public ReminderService (TelegramBot telegramBot, NotificationTaskRepository notificationTaskRepository) {
        this.telegramBot = telegramBot;
        this.notificationTaskRepository = notificationTaskRepository;
    }


    private Logger logger = LoggerFactory.getLogger(ReminderService.class);


    @Scheduled(cron = "${interval-in-cron}")
    public void sendReminder(){
        logger.info("sendReminder was invoked");
        LocalDateTime currentTime = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES);
        List<NotificationTask> taskList = notificationTaskRepository.findAllByDateTimeIs(currentTime);
        taskList.forEach(task -> {
                telegramBot.execute(new SendMessage(task.getChatId(), task.getNote()));
        });
    }
}
