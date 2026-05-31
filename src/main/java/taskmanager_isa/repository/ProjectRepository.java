package taskmanager_isa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import taskmanager_isa.entity.Project;

public interface ProjectRepository extends JpaRepository<Project, Long> {
}
