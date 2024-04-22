package pro.sky.telegrambot.service;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.SendMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import pro.sky.telegrambot.entity.NotificationTask;
import pro.sky.telegrambot.listener.TelegramBotUpdatesListener;
import pro.sky.telegrambot.repository.NotificationTaskRepository;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class ReminderService {
    @Autowired
    private TelegramBot telegramBot;
    @Autowired
    private final NotificationTaskRepository notificationTaskRepository;
    public ReminderService (NotificationTaskRepository notificationTaskRepository) {
        this.notificationTaskRepository = notificationTaskRepository;
    }
    private Logger logger = LoggerFactory.getLogger(ReminderService.class);

    @Scheduled(fixedDelay = 60000)
    public void sendReminder(){
        logger.info("sendReminder was invoked");
        LocalDateTime currentTime = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES);
        List<NotificationTask> taskList = notificationTaskRepository.findAllByDateTimeIs(currentTime);
        taskList.forEach(task -> {
                telegramBot.execute(new SendMessage(task.getChatId(), task.getNote()));
        });
    }
}
