package taskmanager_isa.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import taskmanager_isa.dto.ProjectRequest;
import taskmanager_isa.dto.ProjectResponse;
import taskmanager_isa.entity.Project;
import taskmanager_isa.entity.Task;
import taskmanager_isa.entity.User;
import taskmanager_isa.exception.ResourceNotFoundException;
import taskmanager_isa.repository.CommentRepository;
import taskmanager_isa.repository.ProjectRepository;
import taskmanager_isa.repository.TaskRepository;
import taskmanager_isa.repository.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final TaskRepository taskRepository;
    private final CommentRepository commentRepository;

    public List<ProjectResponse> getAllProjects() {
        return projectRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    public ProjectResponse getProjectById(Long id) {
        return toResponse(projectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found")));
    }

    public ProjectResponse createProject(ProjectRequest request, String username) {
        User owner = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Project project = Project.builder()
                .name(request.getName())
                .description(request.getDescription())
                .owner(owner)
                .build();

        return toResponse(projectRepository.save(project));
    }

    public ProjectResponse updateProject(Long id, ProjectRequest request, String username, boolean isAdmin) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found"));

        if (!isAdmin && !project.getOwner().getUsername().equals(username)) {
            throw new RuntimeException("You can only edit your own projects");
        }

        project.setName(request.getName());
        project.setDescription(request.getDescription());

        return toResponse(projectRepository.save(project));
    }

    @Transactional
    public void deleteProject(Long id) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found"));

        List<Task> tasks = taskRepository.findByProjectId(id);
        for (Task task : tasks) {
            commentRepository.deleteByTaskId(task.getId());
            task.getTags().clear();
        }
        taskRepository.deleteAll(tasks);

        projectRepository.delete(project);
    }

    private ProjectResponse toResponse(Project project) {
        return new ProjectResponse(
                project.getId(),
                project.getName(),
                project.getDescription(),
                project.getOwner().getId(),
                project.getOwner().getUsername()
        );
    }
}
