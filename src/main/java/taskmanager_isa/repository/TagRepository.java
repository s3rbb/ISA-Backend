package taskmanager_isa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import taskmanager_isa.entity.Tag;

public interface TagRepository extends JpaRepository<Tag, Long> {
}
