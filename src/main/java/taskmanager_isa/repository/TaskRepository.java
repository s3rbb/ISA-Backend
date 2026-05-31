package taskmanager_isa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import taskmanager_isa.entity.Task;
import taskmanager_isa.enums.TaskStatus;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByProjectId(Long projectId);
    List<Task> findByStatus(TaskStatus status);
}
