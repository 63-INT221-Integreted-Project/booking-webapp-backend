package sit.int221.bookingproj.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.lang.Nullable;
import sit.int221.bookingproj.entities.Event;
import sit.int221.bookingproj.entities.EventCategory;
import sit.int221.bookingproj.entities.File;

import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.validation.constraints.*;
import java.time.Instant;
import java.time.LocalDateTime;
@Data
public class EventGetDto {

    private Integer eventId;
    private String bookingName;
    private String bookingEmail;

    @JsonFormat(pattern = "yyyy-MM-dd@HH:mm:ss.SSSZ")
    private Instant eventStartTime;

    private Integer eventDuration;

    private String eventNotes;

    private EventCategoryInEventDto eventCategory;

    @OneToOne
    @JoinColumn(name = "fileId" , nullable = true, referencedColumnName = "fileId")
    private File file;

    public Integer getEventId() {
        return eventId;
    }

    public String getBookingName() {
        return bookingName;
    }

    public String getBookingEmail() {
        return bookingEmail;
    }

    public String getEventStartTime() {
        return eventStartTime.toString();
    }

    public Integer getEventDuration() {
        return eventDuration;
    }

    public String getEventNotes() {
        return eventNotes;
    }

    public EventCategoryInEventDto getEventCategory() {
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

    public void setEventStartTime(Instant eventStartTime) {
        this.eventStartTime = eventStartTime;
    }

    public void setEventDuration(Integer eventDuration) {
        this.eventDuration = eventDuration;
    }

    public void setEventNotes(String eventNotes) {
        this.eventNotes = eventNotes;
    }

    public void setEventCategory(EventCategoryInEventDto eventCategoryDto) {
        this.eventCategory = eventCategoryDto;
    }
}
