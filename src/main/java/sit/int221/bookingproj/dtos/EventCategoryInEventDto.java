package sit.int221.bookingproj.dtos;

import lombok.Data;
import org.springframework.stereotype.Service;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Data
public class EventCategoryInEventDto {
    private Integer eventCategoryId;
    private String eventCategoryName;
    private Integer eventDuration;

    public EventCategoryInEventDto(Integer eventCategoryId, String eventCategoryName, Integer eventDuration) {
        this.eventCategoryId = eventCategoryId;
        this.eventCategoryName = eventCategoryName;
        this.eventDuration = eventDuration;
    }

    public Integer getEventCategoryId() {
        return eventCategoryId;
    }

    public void setEventCategoryId(Integer eventCategoryId) {
        this.eventCategoryId = eventCategoryId;
    }

    public String getEventCategoryName() {
        return eventCategoryName;
    }

    public void setEventCategoryName(String eventCategoryName) {
        this.eventCategoryName = eventCategoryName;
    }

    public Integer getEventDuration() {
        return eventDuration;
    }

    public void setEventDuration(Integer eventDuration) {
        this.eventDuration = eventDuration;
    }
}
