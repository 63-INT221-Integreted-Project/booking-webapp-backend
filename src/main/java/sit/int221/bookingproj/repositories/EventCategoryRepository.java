package sit.int221.bookingproj.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sit.int221.bookingproj.entities.EventCategory;

@Repository
public interface EventCategoryRepository extends JpaRepository<EventCategory, Integer> {
    public EventCategory findByEventCategoryId(Integer id);
    public EventCategory findAllByEventCategoryName(String eventCategoryName);

}
