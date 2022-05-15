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
    public List<Event> findAllByEventCategory(Optional<EventCategory> eventCategory);

    public List<Event> findAllByEventDuration(Integer findingNumberDuration);
    public List<Event> findAllByBookingEmailContainingOrBookingNameContaining(String word, String word2, Sort eventStartTime);
    public List<Event> findAllByEventStartTimeBetweenAndEventCategory_EventCategoryName(Instant dateStart, Instant dateEnd, String name, Sort eventStartTime);
    public List<Event> findAllByEventCategory_EventCategoryName(String categoryName, Sort eventStartTime);
    public List<Event> findAllByEventStartTimeBetweenAndEventCategory_EventCategoryNameOrBookingNameContainingOrEventNotesContaining(Instant dateStart, Instant dateEnd, String name, String word1, String word2, Sort eventStartTime);
}
