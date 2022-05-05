package sit.int221.bookingproj.dtos;

import lombok.Data;
import org.springframework.lang.Nullable;

import javax.persistence.Column;
import javax.persistence.Id;

@Data
public class EventCategoryDto {
    @Column(nullable = true)
    private Integer eventCategoryId;
    @Column(nullable = true)
    private String eventCategoryName;

    public EventCategoryDto(@Nullable Integer eventCategoryId,@Nullable String eventCategoryName) {
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
