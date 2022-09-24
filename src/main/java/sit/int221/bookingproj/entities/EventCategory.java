package sit.int221.bookingproj.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.*;

@Entity
@ToString
@Table(name = "event_category")
public class EventCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "eventCategoryId", nullable = false)
    private Integer eventCategoryId;

    @Size(max = 100 , message = "length exceeded the size")
    @Column(name = "eventCategoryName", nullable = false, length = 100, unique = false)
    @NotBlank(message = "can not be blank")
    private String eventCategoryName;

    @Size(max = 500, message = "length exceeded the size")
    @Column(name = "eventCategoryDescription", length = 500)
    private String eventCategoryDescription;

    @Max(value = 480, message = "event duration is out of range")
    @Min(value = 0, message = "event duration is out of range")
    @Column(name = "eventDuration", nullable = false)
    private Integer eventDuration;

    @JsonIgnore
    @OneToMany(mappedBy = "eventCategory" , cascade = CascadeType.DETACH)
    private Set<Event> events = new LinkedHashSet<>();

    @JsonIgnore
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "event_category_owner",
    joinColumns = {
            @JoinColumn(name = "eventCategoryId", referencedColumnName = "eventCategoryId")
    },
    inverseJoinColumns = {
            @JoinColumn(name = "userId", referencedColumnName = "userId")
    })
    public List<User> owner;


    public Set<Event> getEvents() {
        return events;
    }

    @PreRemove
    private void preRemove() {
        for (Event e : events) {
            e.setEventCategory(null);
        }
    }

    public void setEvents(Set<Event> events) {
        this.events = events;
    }

    public Integer getEventDuration() {
        return eventDuration;
    }


    public void setEventDuration(Integer eventDuration) {
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

    public Integer getEventCategoryId() {
        return eventCategoryId;
    }

    public void setEventCategoryId(Integer id) {
        this.eventCategoryId = id;
    }

    public List<User> getOwner() {
        return owner;
    }

    public void setOwner(List<User> owner) {
        this.owner = owner;
    }
}