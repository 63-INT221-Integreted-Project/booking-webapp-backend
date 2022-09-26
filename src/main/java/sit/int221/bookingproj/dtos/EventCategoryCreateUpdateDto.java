package sit.int221.bookingproj.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;
import java.util.ArrayList;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventCategoryCreateUpdateDto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer eventCategoryId;

    @Size(max = 100 , message = "length exceeded the size")
    @Column(name = "eventCategoryName")
    private String eventCategoryName;
    @Size(max = 500, message = "length exceeded the size")
    private String eventCategoryDescription;

    @Max(value = 480, message = "event duration is out of range")
    @Min(value = 1,message = "must higher than 1")
    private Integer eventDuration;

    public ArrayList userId;

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

    public ArrayList getUserId() {
        return userId;
    }

    public void setUserId(ArrayList userId) {
        this.userId = userId;
    }
}
