package sit.int221.bookingproj.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.hibernate.validator.constraints.UniqueElements;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.time.Instant;
import java.time.LocalDateTime;

@Data
public class EventCreateDto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer eventId;
    @NotEmpty(message = "can not empty")
    @Size(max = 100, message = "length exceeded the size")
    private String bookingName;
    @NotEmpty(message = "can not empty")
    @Email(regexp = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?(?:\\.[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?)*$", message = "Email is invalid")
    private String bookingEmail;

    @FutureOrPresent(message = "eventStartTime is NOT in the future")
    private Instant eventStartTime;

    @Max(value = 480,message = "event duration is out of range")
    private Integer eventDuration;

    @Nullable
    @Size(max = 500, message = "length exceeded the size")
    private String eventNotes;
    private Integer eventCategoryId;

    public Integer getEventId() {
        return eventId;
    }

    public String getBookingName() {
        return bookingName;
    }

    public String getBookingEmail() {
        return bookingEmail;
    }

    public Instant getEventStartTime() {
        return eventStartTime;
    }

    public Integer getEventDuration() {
        return eventDuration;
    }

    public String getEventNotes() {
        return eventNotes;
    }

    public Integer getEventCategoryId() {
        return eventCategoryId;
    }

    public void setEventId(Integer eventId) {
        this.eventId = eventId;
    }

    public void setBookingName(String bookingName) {
        this.bookingName = bookingName;
    }

    public void setBookingEmail(String bookingEmail) {
        this.bookingEmail = bookingEmail;
    }

    public void setEventStartTime(Instant eventStartTime) {
        this.eventStartTime = eventStartTime;
    }

    public void setEventDuration(Integer eventDuration) {
        this.eventDuration = eventDuration;
    }

    public void setEventNotes(String eventNotes) {
        this.eventNotes = eventNotes;
    }

    public void setEventCategoryId(Integer eventCategoryId) {
        this.eventCategoryId = eventCategoryId;
    }
}