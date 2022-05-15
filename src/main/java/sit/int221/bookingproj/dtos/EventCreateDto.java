package sit.int221.bookingproj.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.hibernate.validator.constraints.UniqueElements;

import javax.persistence.Column;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.*;
import java.time.Instant;
import java.time.LocalDateTime;

@Data
public class EventCreateDto {
    private Integer eventId;

    @NotEmpty
    private String bookingName;
    @NotEmpty
    private String bookingEmail;
    @NotEmpty
    private Instant eventStartTime;
    @NotEmpty
    private Integer eventDuration;

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