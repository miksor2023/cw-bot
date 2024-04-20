package pro.sky.telegrambot.service;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.SendMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pro.sky.telegrambot.entity.NotificationTask;
import pro.sky.telegrambot.listener.TelegramBotUpdatesListener;
import pro.sky.telegrambot.repository.NotificationTaskRepository;

import java.time.LocalDateTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.regex.Pattern;

@Service
public class TelegramBotService {
    @Autowired
    private final NotificationTaskRepository notificationTaskRepository;


    public TelegramBotService(NotificationTaskRepository notificationTaskRepository) {
        this.notificationTaskRepository = notificationTaskRepository;
    }
    private Logger logger = LoggerFactory.getLogger(TelegramBotService.class);

    public void loadTestTable(){
        logger.info("loadTestTable was invoked");

        notificationTaskRepository.save(new NotificationTask(null, 123L, "test1 привет", LocalDateTime.of(2020, Month.APRIL, 8, 12, 30)));
        notificationTaskRepository.save(new NotificationTask(null, 456L, "test2 привет", LocalDateTime.of(2020, Month.APRIL, 8, 13, 30)));
        notificationTaskRepository.save(new NotificationTask(null, 789L, "test3 привет", LocalDateTime.of(2020, Month.APRIL, 8, 14, 30)));
        notificationTaskRepository.save(new NotificationTask(null, 101112L, "test4 привет", LocalDateTime.of(2020, Month.APRIL, 8, 15, 30)));
        notificationTaskRepository.save(new NotificationTask(null, 131415L, "test5 привет", LocalDateTime.of(2020, Month.APRIL, 8, 16, 30)));
    }

    public void clearTable(){
        logger.info("claerTable was invoked");

        notificationTaskRepository.deleteAll();
    }

    public void printTable(TelegramBot bot, long chatId) {
        logger.info("printTable was invoked");
        List<NotificationTask> taskList = notificationTaskRepository.findAll();
        bot.execute(new SendMessage(chatId, taskList.toString()));

    }

    public void sayHallo(TelegramBot bot, long chatId) {
        logger.info("sayHallo was invoked");
        bot.execute(new SendMessage(chatId, "Maşallah! Maşallah!"));
    }


    public boolean textMaches (String text) {
        logger.info("textMatcher was invoked");
        return text.matches("([0-9\\.\\:\\s]{16})(\\s)([\\W+]+)");
//        String regex = "([0-9\.\:\s]{16})(\s)([\W+]+)";
//        Pattern pattern = Pattern.compile(regex);
//        return pattern.matcher(text);
    }

    public void putEntry(long chatId, String text) {
        logger.info("putEntity was invoked");
        NotificationTask notificationTask = new NotificationTask();
        String dateTimeSubstring = text.substring(0, 16);
        String noteSubstring = text.substring(17);
        LocalDateTime dateTime = LocalDateTime.parse(dateTimeSubstring, DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"));

        notificationTask.setChatId(chatId);
        notificationTask.setNote(noteSubstring);
        notificationTask.setDateTime(dateTime);

        notificationTaskRepository.save(notificationTask);
    }
}
