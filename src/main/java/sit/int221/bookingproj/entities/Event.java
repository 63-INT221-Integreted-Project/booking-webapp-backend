package sit.int221.bookingproj.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.intellij.lang.annotations.Pattern;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.time.Instant;
import java.time.LocalDateTime;

@Entity
@Table(name = "event")
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "eventId", nullable = false)
    private Integer eventId;

    @Column(name = "bookingName", nullable = false, length = 100)
    private String bookingName;
    @Column(name = "bookingEmail", nullable = false)
    private String bookingEmail;

    @Column(name = "eventStartTime", nullable = false)
    private Instant eventStartTime;

    @Column(name = "eventDuration", nullable = false)
    private Integer eventDuration;

    @Column(name = "eventNotes", length = 500, nullable = true)
    private String eventNotes;

    @ManyToOne(fetch = FetchType.EAGER, optional = true )
    @JoinColumn(name = "eventCategoryId", nullable = true, referencedColumnName = "eventCategoryId")
    private EventCategory eventCategory;


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

    public Instant getEventStartTime() {
        return eventStartTime;
    }

    public void setEventStartTime(Instant eventStartTime) {
        this.eventStartTime = eventStartTime;
    }

    public EventCategory getEventCategory() {
        return eventCategory;
    }

    public void setEventCategory(EventCategory eventCategory) {
        this.eventCategory = eventCategory;
    }

    public String getBookingName() {
        return bookingName;
    }

    public void setBookingName(String eventBookingName) {
        this.bookingName= eventBookingName;
    }

    public String getBookingEmail() {
        return bookingEmail;
    }

    public void setBookingEmail(String eventBookingEmail) {
        this.bookingEmail = eventBookingEmail;
    }

    public Integer getEventId() {
        return eventId;
    }

    public void setEventId(Integer id) {
        this.eventId = id;
    }
}