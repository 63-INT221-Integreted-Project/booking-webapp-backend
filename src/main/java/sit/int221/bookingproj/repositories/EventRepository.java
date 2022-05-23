package sit.int221.bookingproj.repositories;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sit.int221.bookingproj.entities.Event;
import sit.int221.bookingproj.entities.EventCategory;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface EventRepository extends JpaRepository<Event, Integer> {
    public List<Event> findAllByEventStartTimeBetween(Instant start, Instant end, Sort eventStartTime);
    public List<Event> findAllByEventStartTimeLessThanEqualAndEventStartTimeGreaterThanEqual(Instant end, Instant start, Sort eventStartTime);
    public List<Event> findAllByEventCategory(Optional<EventCategory> eventCategory);
    public List<Event> findAllByBookingEmailContainingOrBookingNameContaining(String word, String word2, Sort eventStartTime);
    public List<Event> findAllByEventStartTimeBetweenAndEventCategory_EventCategoryName(Instant dateStart, Instant dateEnd, String name, Sort eventStartTime);
    public Event findAllByEventStartTimeBetweenAndEventCategory_EventCategoryId(Instant dateStart, Instant dateEnd, Integer id, Sort eventStartTime);

}
