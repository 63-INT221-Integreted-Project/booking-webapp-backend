package sit.int221.bookingproj.entities;

import javax.persistence.*;

@Entity
@Table(name = "events")
public class Event {
    @EmbeddedId
    private EventId id;

    @MapsId("eventCategoryId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "eventCategoryId", nullable = false)
    private EventCategory eventCategory;

    @Column(name = "bookingName", length = 45)
    private String bookingName;

    @Column(name = "bookingEmail", length = 45)
    private String bookingEmail;

    @Column(name = "eventStartTime", length = 45)
    private String eventStartTime;

    @Lob
    @Column(name = "eventNotes")
    private String eventNotes;

    public String getEventNotes() {
        return eventNotes;
    }

    public void setEventNotes(String eventNotes) {
        this.eventNotes = eventNotes;
    }

    public String getEventStartTime() {
        return eventStartTime;
    }

    public void setEventStartTime(String eventStartTime) {
        this.eventStartTime = eventStartTime;
    }

    public String getBookingEmail() {
        return bookingEmail;
    }

    public void setBookingEmail(String bookingEmail) {
        this.bookingEmail = bookingEmail;
    }

    public String getBookingName() {
        return bookingName;
    }

    public void setBookingName(String bookingName) {
        this.bookingName = bookingName;
    }

    public EventCategory getEventCategory() {
        return eventCategory;
    }

    public void setEventCategory(EventCategory eventCategory) {
        this.eventCategory = eventCategory;
    }

    public EventId getId() {
        return id;
    }

    public void setId(EventId id) {
        this.id = id;
    }
}