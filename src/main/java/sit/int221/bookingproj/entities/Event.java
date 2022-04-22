package sit.int221.bookingproj.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

@Entity
@Table(name = "events")
public class Event {
    @Id
    @Column(name = "eventId", length = 16)
    private String id;

    @Column(name = "bookingName", length = 45)
    private String bookingName;

    @Column(name = "bookingEmail", length = 45)
    private String bookingEmail;

    @Column(name = "eventStartTime", length = 45)
    private String eventStartTime;

    @Lob
    @Column(name = "eventNotes")
    private String eventNotes;

//    @JsonIgnore
//    @ManyToOne(fetch = FetchType.LAZY, optional = false)
//    @JoinColumn(name = "eventCategoryId", nullable = false)
//    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @Column(name = "eventCategoryId")
    private String eventCategoryId;

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

//    public EventCategory getEventCategory() {
//        return eventCategory;
//    }
//
//    public void setEventCategory(EventCategory eventCategory) {
//        this.eventCategory = eventCategory;
//    }
    public String getEventCategoryId() {
        return eventCategoryId;
    }

    public void setEventCategoryId(String eventCategory) {
        this.eventCategoryId = eventCategory;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}