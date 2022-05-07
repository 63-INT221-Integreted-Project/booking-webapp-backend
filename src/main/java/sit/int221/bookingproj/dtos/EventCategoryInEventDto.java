package sit.int221.bookingproj.dtos;

import lombok.Data;
import org.springframework.stereotype.Service;

@Data
public class EventCategoryInEventDto {
    private Integer eventCategoryId;
    private String eventCategoryName;

    public EventCategoryInEventDto(Integer eventCategoryId, String eventCategoryName) {
        this.eventCategoryId = eventCategoryId;
        this.eventCategoryName = eventCategoryName;
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
}
