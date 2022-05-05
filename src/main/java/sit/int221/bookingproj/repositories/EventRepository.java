package sit.int221.bookingproj.repositories;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sit.int221.bookingproj.entities.Event;
import sit.int221.bookingproj.entities.EventCategory;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface EventRepository extends JpaRepository<Event, Integer> {
//    public List<Event> findAllByEventStartTimeStartsWith(String date);
    public List<Event> findAllByEventStartTimeBetween(LocalDateTime start, LocalDateTime end, Sort eventStartTime);
    public List<Event> findAllByEventCategory(Optional<EventCategory> eventCategory);
    public List<Event> findAllByBookingEmailContainingOrBookingNameContainingOrEventNotesContaining(String bookingEmail, String bookingName, String eventNote);
    public List<Event> findAllByEventDuration(Integer findingNumberDuration);
    public List<Event> findAllByEventStartTimeAfter(LocalDateTime localDateTime);
}
