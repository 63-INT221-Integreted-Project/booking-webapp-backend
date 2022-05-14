package sit.int221.bookingproj.dtos;

import lombok.Data;
import org.springframework.lang.Nullable;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.validation.constraints.*;

@Data
public class EventCategoryDto {
    private Integer eventCategoryId;
    @NotEmpty
    @Size(min = 0, max = 100, message = "eventCategoryName is invalid")
    private String eventCategoryName;
    @NotEmpty
    @Size(min = 0, max = 500, message = "eventCategoryDescription is invalid")
    private String eventCategoryDescription;
    @NotEmpty
    @Min(value = 0, message = "the value can not under than 0")
    @Max(value = 480 , message = "the value can not higher than 480")
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
