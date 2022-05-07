package sit.int221.bookingproj.dtos;

import lombok.Data;
import org.springframework.lang.Nullable;

import javax.persistence.Column;
import javax.persistence.Id;

@Data
public class EventCategoryDto {
    private Integer eventCategoryId;
    private String eventCategoryName;
    private String eventCategoryDescription;
    private Integer eventDuration;

    public EventCategoryDto(Integer eventCategoryId, String eventCategoryName) {
        this.eventCategoryId = eventCategoryId;
        this.eventCategoryName = eventCategoryName;
    }

    public EventCategoryDto() {

    }


    public Integer getEventCategoryId() {
        return eventCategoryId;
    }

    public void setEventCategoryId(Integer eventCategoryId) {
        this.eventCategoryId = eventCategoryId;
    }

    public String getEventCategoryName(String eventCategoryName) {
        return this.eventCategoryName;
    }

    public void setEventCategoryName(String eventCategoryName) {
        this.eventCategoryName = eventCategoryName;
    }

    public String getEventCategoryDescription() {
        return eventCategoryDescription;
    }

    public void setEventCategoryDescription(String eventCategoryDescription) {
        this.eventCategoryDescription = eventCategoryDescription;
    }

    public Integer getEventDuration() {
        return eventDuration;
    }

    public void setEventDuration(Integer eventDuration) {
        this.eventDuration = eventDuration;
    }
}
