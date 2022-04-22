package sit.int221.bookingproj.entities;

import org.hibernate.Hibernate;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Entity;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class EventId implements Serializable {
    private static final long serialVersionUID = -973891763181610085L;
    @Column(name = "eventId", nullable = false, length = 16)
    private String eventId;
    @Column(name = "eventCategoryId", nullable = false, length = 16)
    private String eventCategoryId;

    public String getEventCategoryId() {
        return eventCategoryId;
    }

    public void setEventCategoryId(String eventCategoryId) {
        this.eventCategoryId = eventCategoryId;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(eventId, eventCategoryId);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        EventId entity = (EventId) o;
        return Objects.equals(this.eventId, entity.eventId) &&
                Objects.equals(this.eventCategoryId, entity.eventCategoryId);
    }
}