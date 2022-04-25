package sit.int221.bookingproj.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.tomcat.jni.Local;
import org.springframework.context.annotation.Primary;

import javax.persistence.*;
import java.time.Instant;
import java.time.LocalDateTime;

@Entity
@Table(name = "events")
public class Event {
    @Id
    @JsonProperty("eventId")

    @Column(name = "eventId", nullable = false)
    private String eventId;

    @JsonIgnore
    @JsonProperty("eventCategoryId")
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "eventCategoryId", referencedColumnName = "eventCategoryId")
    private EventCategory eventCategory;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @Column(name = "eventStartTime")
    private LocalDateTime eventStartTime;

    @Column(name = "eventDuration")
    private Integer eventDuration;

    @Column(name = "eventNotes", length = 500)
    private String eventNotes;

//    @JsonIgnore
//    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JsonIgnore
    @JsonProperty("booking")
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "bookingId", nullable = false, referencedColumnName = "bookingId")
    private Booking booking;

    public Booking getBooking() {
        return booking;
    }

    public void setBooking(Booking booking) {
        this.booking = booking;
    }

    public String getEventNotes() {
        return eventNotes;
    }

    public void setEventNotes(String eventNotes) {
        this.eventNotes = eventNotes;
    }

    public Integer getEventDuration() {
        return eventDuration;
    }

    public void setEventDuration(Integer eventDuration) {
        this.eventDuration = eventDuration;
    }

    public LocalDateTime getEventStartTime() {
        return eventStartTime;
    }

    public void setEventStartTime(LocalDateTime eventStartTime) {
        this.eventStartTime = eventStartTime;
    }

    public EventCategory getEventCategory() {
        return eventCategory;
    }

    public void setEventCategory(EventCategory eventCategory) {
        this.eventCategory = eventCategory;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String id) {
        this.eventId = id;
    }
}