package sit.int221.bookingproj.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.validation.constraints.FutureOrPresent;
import java.time.Instant;
import java.time.LocalDateTime;

@Data
public class EventUpdateDto {
    @FutureOrPresent
    private Instant eventStartTime;
    private String eventNotes;

    public String getEventNotes() {
        return eventNotes;
    }

    public void setEventNotes(String eventNotes) {
        this.eventNotes = eventNotes;
    }

    public Instant getEventStartTime() {
        return eventStartTime;
    }

    public void setEventStartTime(Instant eventStartTime) {
        this.eventStartTime = eventStartTime;
    }
}
