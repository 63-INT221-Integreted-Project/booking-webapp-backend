package sit.int221.bookingproj.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sit.int221.bookingproj.entities.User;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    public User findAllByName(String name);
    public User findAllByEmail(String email);

    public User findUserByEmail(String email);

    public List<User> findAllByRole(String role);
}
