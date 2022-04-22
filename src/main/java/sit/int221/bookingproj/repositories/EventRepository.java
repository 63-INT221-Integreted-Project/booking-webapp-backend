package sit.int221.bookingproj.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import sit.int221.bookingproj.entities.Event;

public interface EventRepository extends JpaRepository<Event,Integer> {
}
