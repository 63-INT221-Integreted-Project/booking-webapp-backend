package sit.int221.bookingproj.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import sit.int221.bookingproj.entities.Booking;

public interface BookingRepository extends JpaRepository<Booking, String> {
}
