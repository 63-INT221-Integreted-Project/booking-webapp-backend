package sit.int221.bookingproj.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import sit.int221.bookingproj.entities.EventCategory;

public interface EventCategoryRepository extends JpaRepository<EventCategory, String> {
}
