package sit.int221.bookingproj.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sit.int221.bookingproj.entities.File;

@Repository
public interface FileRepository extends JpaRepository<File, Integer> {
}
