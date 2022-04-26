package sit.int221.bookingproj.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.annotations.Fetch;

import javax.persistence.*;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "event_categories")
public class EventCategory {
    @Id
    @Column(name = "eventCategoryId", nullable = false, length = 16)
    private String eventCategoryId;

    @Column(name = "eventCategoryName", length = 100)
    private String eventCategoryName;

    @Column(name = "eventCategoryDescription", length = 500)
    private String eventCategoryDescription;

    @Column(name = "eventCategoryDuration")
    private Integer eventCategoryDuration;


    @JsonIgnore
    @OneToMany(mappedBy = "eventCategory", fetch = FetchType.EAGER)
    private Set<Event> events = new LinkedHashSet<>();

    public Set<Event> getEvents() {
        return events;
    }

    public void setEvents(Set<Event> events) {
        this.events = events;
    }

    public Integer getEventCategoryDuration() {
        return eventCategoryDuration;
    }

    public void setEventCategoryDuration(Integer eventCategoryDuration) {
        this.eventCategoryDuration = eventCategoryDuration;
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

    public String getEventCategoryId() {
        return eventCategoryId;
    }

    public void setEventCategoryId(String id) {
        this.eventCategoryId = id;
    }
}