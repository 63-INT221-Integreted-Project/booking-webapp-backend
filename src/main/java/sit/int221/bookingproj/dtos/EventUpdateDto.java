package sit.int221.bookingproj.dtos;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class EventUpdateDto {

    private LocalDateTime eventStartTime;
    private Integer eventDuration;

    public Integer getEventDuration() {
        return eventDuration;
    }

    public void setEventDuration(Integer eventDuration) {
        this.eventDuration = eventDuration;
    }

    public LocalDateTime getEventStartTime() {
        return eventStartTime;
    }

    public void setEventStartTime(LocalDateTime eventStartTime) {
        this.eventStartTime = eventStartTime;
    }
}
