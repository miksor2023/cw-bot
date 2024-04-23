package pro.sky.telegrambot.service;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.SendMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pro.sky.telegrambot.entity.NotificationTask;
import pro.sky.telegrambot.repository.NotificationTaskRepository;

import java.time.LocalDateTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;


@Service
public class TelegramBotService {
    @Autowired
    private TelegramBot telegramBot;
    @Autowired
    private final NotificationTaskRepository notificationTaskRepository;


    public TelegramBotService(NotificationTaskRepository notificationTaskRepository) {
        this.notificationTaskRepository = notificationTaskRepository;
    }
    private Logger logger = LoggerFactory.getLogger(TelegramBotService.class);

    public void loadTestTable(Long chatId){//метод использовался для отладки - решил пока не удалять
        logger.info("loadTestTable was invoked");

        notificationTaskRepository.save(new NotificationTask(null, 123L, "test1 привет", "01.01.2022 20:00"));
        notificationTaskRepository.save(new NotificationTask(null, 456L, "test2 привет", "01.01.2022 20:00"));
        notificationTaskRepository.save(new NotificationTask(null, 789L, "test3 привет", "01.01.2022 20:00"));
        notificationTaskRepository.save(new NotificationTask(null, 101112L, "test4 привет", "01.01.2022 20:00"));
        notificationTaskRepository.save(new NotificationTask(null, 131415L, "test5 привет", "01.01.2022 20:00"));
        telegramBot.execute(new SendMessage(chatId, "Table loaded"));
    }

    public void clearTable(long chatId){
        logger.info("clearTable was invoked");
        telegramBot.execute(new SendMessage(chatId, "Table cleared"));
        notificationTaskRepository.deleteAll();
    }

    public void printTable(long chatId) {
        logger.info("printTable was invoked");
        List<NotificationTask> taskList = notificationTaskRepository.findAll();
        telegramBot.execute(new SendMessage(chatId, taskList.toString()));

    }

    public void sayHallo(long chatId) {//реализует ответ на приветствие, метод сделан ради фана
        logger.info("sayHallo was invoked");
    }


    public boolean textMaches (String text) {
        logger.info("textMatcher was invoked");
        return text.matches("([0-9\\.\\:\\s]{16})(\\s)([\\W+]+)");
    }

    public void putEntry(long chatId, String text) {//занести запись в БД. получает chatId и строку
        logger.info("putEntity was invoked");
        NotificationTask notificationTask = new NotificationTask();
        String dateTimeSubstring = text.substring(0, 16);
        String noteSubstring = text.substring(17);
        LocalDateTime dateTime = null;
        try {
            dateTime = LocalDateTime.parse(dateTimeSubstring, DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"));
        } catch (DateTimeParseException e) {
            e.printStackTrace();
        }
        if(dateTime.isAfter(LocalDateTime.now())) {
            notificationTask.setChatId(chatId);
            notificationTask.setNote(noteSubstring);
            notificationTask.setDateTime(dateTime.toString());
            notificationTaskRepository.save(notificationTask);
            telegramBot.execute(new SendMessage(chatId, "Entry added"));
        } else if (dateTime != null) {
            telegramBot.execute(new SendMessage(chatId, "The date must be later than the current one"));
        } else {
            telegramBot.execute(new SendMessage(chatId, "Parsing date error!\n Please chek date/time format"));
        }
    }

    public void sendChosenTasks(List<Long> enteredIds) {//получает список Id записей, для которых затем осуществляется
                                                        //отправка текстового сообщения, не дожидаясь момента из dateTime

        List<NotificationTask> entries = notificationTaskRepository.findAll();
        entries.forEach(entry -> {
            if(enteredIds.contains(entry.getId())){
                telegramBot.execute(new SendMessage(entry.getChatId(), entry.getNote()));
            }
        });
    }

    public List<Long> getIdList() {
        return notificationTaskRepository.findIds();
    }

}
