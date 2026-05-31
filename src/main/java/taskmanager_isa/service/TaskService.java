package taskmanager_isa.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import taskmanager_isa.dto.TagResponse;
import taskmanager_isa.dto.TaskRequest;
import taskmanager_isa.dto.TaskResponse;
import taskmanager_isa.entity.Project;
import taskmanager_isa.entity.Tag;
import taskmanager_isa.entity.Task;
import taskmanager_isa.entity.User;
import taskmanager_isa.enums.TaskStatus;
import taskmanager_isa.exception.ResourceNotFoundException;
import taskmanager_isa.repository.CommentRepository;
import taskmanager_isa.repository.ProjectRepository;
import taskmanager_isa.repository.TagRepository;
import taskmanager_isa.repository.TaskRepository;
import taskmanager_isa.repository.UserRepository;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final TagRepository tagRepository;
    private final CommentRepository commentRepository;

    public List<TaskResponse> getAllTasks(String status, Long projectId) {
        List<Task> tasks;

        if (status != null) {
            tasks = taskRepository.findByStatus(TaskStatus.valueOf(status));
        } else if (projectId != null) {
            tasks = taskRepository.findByProjectId(projectId);
        } else {
            tasks = taskRepository.findAll();
        }

        return tasks.stream().map(this::toResponse).toList();
    }

    public TaskResponse getTaskById(Long id) {
        return toResponse(taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found")));
    }

    public TaskResponse createTask(TaskRequest request, String username) {
        Project project = projectRepository.findById(request.getProjectId())
                .orElseThrow(() -> new ResourceNotFoundException("Project not found"));

        User creator = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Set<Tag> tags = new HashSet<>();
        if (request.getTagIds() != null && !request.getTagIds().isEmpty()) {
            tags = new HashSet<>(tagRepository.findAllById(request.getTagIds()));
        }

        Task task = Task.builder()
                .title(request.getTitle())
                .status(TaskStatus.valueOf(request.getStatus()))
                .project(project)
                .createdBy(creator)
                .tags(tags)
                .build();

        return toResponse(taskRepository.save(task));
    }

    public TaskResponse updateTask(Long id, TaskRequest request, String username, boolean isAdmin) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found"));

        if (!isAdmin && (task.getCreatedBy() == null || !task.getCreatedBy().getUsername().equals(username))) {
            throw new RuntimeException("You can only edit your own tasks");
        }

        Project project = projectRepository.findById(request.getProjectId())
                .orElseThrow(() -> new ResourceNotFoundException("Project not found"));

        Set<Tag> tags = new HashSet<>();
        if (request.getTagIds() != null && !request.getTagIds().isEmpty()) {
            tags = new HashSet<>(tagRepository.findAllById(request.getTagIds()));
        }

        task.setTitle(request.getTitle());
        task.setStatus(TaskStatus.valueOf(request.getStatus()));
        task.setProject(project);
        task.setTags(tags);

        return toResponse(taskRepository.save(task));
    }

    @Transactional
    public void deleteTask(Long id, String username, boolean isAdmin) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found"));

        if (!isAdmin && (task.getCreatedBy() == null || !task.getCreatedBy().getUsername().equals(username))) {
            throw new RuntimeException("You can only delete your own tasks");
        }

        commentRepository.deleteByTaskId(id);
        taskRepository.delete(task);
    }

    private TaskResponse toResponse(Task task) {
        Set<TagResponse> tagResponses = task.getTags().stream()
                .map(tag -> new TagResponse(
                        tag.getId(),
                        tag.getName(),
                        tag.getCreatedBy() != null ? tag.getCreatedBy().getId() : null,
                        tag.getCreatedBy() != null ? tag.getCreatedBy().getUsername() : null
                ))
                .collect(Collectors.toSet());

        return new TaskResponse(
                task.getId(),
                task.getTitle(),
                task.getStatus().name(),
                task.getProject().getId(),
                task.getProject().getName(),
                task.getCreatedBy() != null ? task.getCreatedBy().getId() : null,
                task.getCreatedBy() != null ? task.getCreatedBy().getUsername() : null,
                tagResponses
        );
    }
}
