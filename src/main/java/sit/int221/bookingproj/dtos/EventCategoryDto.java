package sit.int221.bookingproj.dtos;

import lombok.Data;

@Data
public class EventCategoryDto {
    private Integer eventCategoryId;
    private String eventCategoryName;

    public EventCategoryDto(Integer eventCategoryId, String eventCategoryName) {
        this.eventCategoryId = eventCategoryId;
        this.eventCategoryName = eventCategoryName;
    }

    public Integer getEventCategoryId() {
        return eventCategoryId;
    }

    public String getEventCategoryName() {
        return eventCategoryName;
    }

    public void setEventCategoryId(Integer eventCategoryId) {
        this.eventCategoryId = eventCategoryId;
    }

    public void setEventCategoryName(String eventCategoryName) {
        this.eventCategoryName = eventCategoryName;
    }
}
