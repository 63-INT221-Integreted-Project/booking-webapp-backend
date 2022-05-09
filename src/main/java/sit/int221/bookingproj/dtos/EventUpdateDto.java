package sit.int221.bookingproj.dtos;

import lombok.Data;

@Data
public class EventUpdateDto {
    private Integer eventDuration;
    private String eventNotes;

    public Integer getEventDuration() {
        return eventDuration;
    }

    public void setEventDuration(Integer eventDuration) {
        this.eventDuration = eventDuration;
    }

    public String getEventNotes() {
        return eventNotes;
    }

    public void setEventNotes(String eventNotes) {
        this.eventNotes = eventNotes;
    }
}
