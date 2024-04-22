package pro.sky.telegrambot.entity;




import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
public class NotificationTask {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Long chatId;
    private String note;
    private LocalDateTime dateTime;

    public NotificationTask(){}

    public NotificationTask(Long id, Long chatId, String note, LocalDateTime dateTime) {
        this.id = id;
        this.chatId = chatId;
        this.note = note;
        this.dateTime = dateTime;
    }

    public Long getId() {
        return id;
    }


    public Long getChatId() {
        return chatId;
    }

    public void setChatId(Long chatId) {
        this.chatId = chatId;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof NotificationTask)) return false;
        NotificationTask that = (NotificationTask) o;
        return getId() == that.getId() && getChatId() == that.getChatId() && Objects.equals(getNote(), that.getNote()) && Objects.equals(getDateTime(), that.getDateTime());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getChatId(), getNote(), getDateTime());
    }

    @Override
    public String toString() {
        return "id=" + id +
                ", chatId=" + chatId +
                ", note='" + note +
                ", dateTime=" + dateTime;
    }
}