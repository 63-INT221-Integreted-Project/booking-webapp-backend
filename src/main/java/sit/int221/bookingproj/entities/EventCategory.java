package sit.int221.bookingproj.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "event_categories")
public class EventCategory {
    @Id
    @Column(name = "eventCategoryId", nullable = false, length = 16)
    private String id;

    @Column(name = "eventCategoryName", length = 45)
    private String eventCategoryName;

    @Column(name = "eventCategoryDescription", length = 45)
    private String eventCategoryDescription;

    @Column(name = "eventDuration", length = 45)
    private String eventDuration;

    @JsonIgnore
    @OneToMany(mappedBy = "eventCategoryId")
    private Set<Event> events = new LinkedHashSet<>();

    public Set<Event> getEvents() {
        return events;
    }

    public void setEvents(Set<Event> events) {
        this.events = events;
    }

    public String getEventDuration() {
        return eventDuration;
    }

    public void setEventDuration(String eventDuration) {
        this.eventDuration = eventDuration;
    }

    public String getEventCategoryDescription() {
        return eventCategoryDescription;
    }

    public void setEventCategoryDescription(String eventCategoryDescription) {
        this.eventCategoryDescription = eventCategoryDescription;
    }

    public String getEventCategoryName() {
        return eventCategoryName;
    }

    public void setEventCategoryName(String eventCategoryName) {
        this.eventCategoryName = eventCategoryName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}