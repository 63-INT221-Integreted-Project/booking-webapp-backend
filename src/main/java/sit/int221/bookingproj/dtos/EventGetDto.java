package sit.int221.bookingproj.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import sit.int221.bookingproj.entities.Event;
import sit.int221.bookingproj.entities.EventCategory;

import java.time.LocalDateTime;
@Data
public class EventGetDto {
    private Integer eventId;
    private String bookingName;
    private String bookingEmail;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventStartTime;
    private Integer eventDuration;
    private String eventNotes;
    private EventCategoryDto eventCategory;

    public Integer getEventId() {
        return eventId;
    }

    public String getBookingName() {
        return bookingName;
    }

    public String getBookingEmail() {
        return bookingEmail;
    }

    public LocalDateTime getEventStartTime() {
        return eventStartTime;
    }

    public Integer getEventDuration() {
        return eventDuration;
    }

    public String getEventNotes() {
        return eventNotes;
    }

    public EventCategoryDto getEventCategory() {
        return eventCategory;
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

    public void setEventStartTime(LocalDateTime eventStartTime) {
        this.eventStartTime = eventStartTime;
    }

    public void setEventDuration(Integer eventDuration) {
        this.eventDuration = eventDuration;
    }

    public void setEventNotes(String eventNotes) {
        this.eventNotes = eventNotes;
    }

    public void setEventCategory(EventCategoryDto eventCategoryDto) {
        this.eventCategory = eventCategoryDto;
    }
}
