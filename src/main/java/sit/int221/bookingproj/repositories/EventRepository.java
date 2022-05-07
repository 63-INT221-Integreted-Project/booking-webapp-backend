package sit.int221.bookingproj.repositories;

import ch.qos.logback.core.encoder.EchoEncoder;
import org.apache.tomcat.jni.Local;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sit.int221.bookingproj.entities.Event;
import sit.int221.bookingproj.entities.EventCategory;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface EventRepository extends JpaRepository<Event, Integer> {
//    public List<Event> findAllByEventStartTimeStartsWith(String date);
    public List<Event> findAllByEventStartTimeBetween(LocalDateTime start, LocalDateTime end, Sort eventStartTime);
    public List<Event> findAllByEventCategory(Optional<EventCategory> eventCategory);
    public List<Event> findAllByEventCategory_EventCategoryIdAndEventStartTimeBetweenAndBookingEmailContainsAndBookingNameContainsAndEventNotesContains(Integer id, LocalDateTime startTime , LocalDateTime endTime, String bookingEmail, String bookingName, String eventNote);
    public List<Event> findAllByEventDuration(Integer findingNumberDuration);
    public List<Event> findAllByEventStartTimeAfter(LocalDateTime localDateTime);
    public List<Event> findAllByBookingEmailContainingOrBookingNameContaining(String word, String word2);
    public List<Event> findAllByEventStartTimeBetweenAndEventCategory_EventCategoryName(LocalDateTime dateStart, LocalDateTime dateEnd, String name, Sort eventStartTime);
    public List<Event> findAllByEventCategory_EventCategoryName(String categoryName, Sort eventStartTime);
    public List<Event> findAllByEventStartTimeBetweenAndEventCategory_EventCategoryNameOrBookingNameContainingOrEventNotesContaining(LocalDateTime dateStart, LocalDateTime dateEnd, String name, String word1, String word2, Sort eventStartTime);
}
