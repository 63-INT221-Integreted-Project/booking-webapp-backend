package sit.int221.bookingproj.dtos;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class EventUpdateDto {

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
