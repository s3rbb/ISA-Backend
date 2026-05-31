package taskmanager_isa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import taskmanager_isa.entity.RefreshToken;
import taskmanager_isa.entity.User;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByToken(String token);
    Optional<RefreshToken> findByUser(User user);
}
