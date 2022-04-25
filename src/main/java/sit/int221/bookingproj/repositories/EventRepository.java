package sit.int221.bookingproj.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sit.int221.bookingproj.entities.Event;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
@Repository
public interface EventRepository extends JpaRepository<Event, String> {
    public List<Event> findAllByEventStartTimeStartsWith(String date);
    public List<Event> findAllByEventStartTimeBetween(LocalDateTime start, LocalDateTime end);
}
