package sit.int221.bookingproj.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import sit.int221.bookingproj.entities.EventCategory;

import java.time.LocalDateTime;
import java.util.Optional;

@Data
public class EventDto {
    private Integer eventId;
    private String bookingName;
    private String bookingEmail;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventStartTime;
    private Integer eventDuration;
    private String eventNotes;
    private Optional<EventCategory> eventCategory;

    public EventDto(Integer eventId, String bookingName, String bookingEmail, LocalDateTime eventStartTime, Integer eventDuration, String eventNotes, Optional<EventCategory> eventCategory) {
        this.eventId = eventId;
        this.bookingName = bookingName;
        this.bookingEmail = bookingEmail;
        this.eventStartTime = eventStartTime;
        this.eventDuration = eventDuration;
        this.eventNotes = eventNotes;
        this.eventCategory = eventCategory;
    }

    public Integer getEventId() {
        return eventId;
    }

    public void setEventId(Integer eventId) {
        this.eventId = eventId;
    }

    public String getBookingName() {
        return bookingName;
    }

    public void setBookingName(String bookingName) {
        this.bookingName = bookingName;
    }

    public String getBookingEmail() {
        return bookingEmail;
    }

    public void setBookingEmail(String bookingEmail) {
        this.bookingEmail = bookingEmail;
    }

    public LocalDateTime getEventStartTime() {
        return eventStartTime;
    }

    public void setEventStartTime(LocalDateTime eventStartTime) {
        this.eventStartTime = eventStartTime;
    }

    public Integer getEventDuration() {
        return eventDuration;
    }

    public void setEventDuration(Integer eventDuration) {
        this.eventDuration = eventDuration;
    }

    public String getEventNotes() {
        return eventNotes;
    }

    public void setEventNotes(String eventNotes) {
        this.eventNotes = eventNotes;
    }

    public Optional<EventCategory> getEventCategory() {
        return eventCategory;
    }

    public void setEventCategory(EventCategory eventCategory) {
        this.eventCategory = Optional.ofNullable(eventCategory);
    }
}
