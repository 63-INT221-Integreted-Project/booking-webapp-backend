package sit.int221.bookingproj.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.lang.Nullable;

import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.Size;
import java.time.Instant;
import java.time.LocalDateTime;

@Data
public class EventUpdateDto {
    @FutureOrPresent(message = "eventStartTime is NOT in the future")
    private Instant eventStartTime;
    @Nullable
    @Size(max = 500, message = "length exceeded the size")
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
