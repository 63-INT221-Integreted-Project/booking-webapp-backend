package sit.int221.bookingproj.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sit.int221.bookingproj.entities.Event;
import sit.int221.bookingproj.entities.EventCategory;
import sit.int221.bookingproj.entities.User;

import java.util.List;

@Repository
public interface EventCategoryRepository extends JpaRepository<EventCategory, Integer> {
    public EventCategory findAllByEventCategoryName(String eventCategoryName);

    public List<EventCategory> findAllByOwner(User user);
}
