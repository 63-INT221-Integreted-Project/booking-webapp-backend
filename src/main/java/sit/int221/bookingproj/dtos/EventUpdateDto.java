package sit.int221.bookingproj.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class EventUpdateDto {

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventStartTime;
    private String eventNotes;

    public String getEventNotes() {
        return eventNotes;
    }

    public void setEventNotes(String eventNotes) {
        this.eventNotes = eventNotes;
    }

    public LocalDateTime getEventStartTime() {
        return eventStartTime;
    }

    public void setEventStartTime(LocalDateTime eventStartTime) {
        this.eventStartTime = eventStartTime;
    }
}
