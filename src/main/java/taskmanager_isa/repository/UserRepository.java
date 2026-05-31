package taskmanager_isa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import taskmanager_isa.entity.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
}
