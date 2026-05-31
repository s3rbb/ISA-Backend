package taskmanager_isa.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import taskmanager_isa.dto.TaskRequest;
import taskmanager_isa.dto.TaskResponse;
import taskmanager_isa.service.TaskService;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    @GetMapping
    public ResponseEntity<List<TaskResponse>> getTasks(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) Long projectId) {
        return ResponseEntity.ok(taskService.getAllTasks(status, projectId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskResponse> getTask(@PathVariable Long id) {
        return ResponseEntity.ok(taskService.getTaskById(id));
    }

    @PostMapping
    public ResponseEntity<TaskResponse> createTask(@Valid @RequestBody TaskRequest request,
                                                   @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(taskService.createTask(request, userDetails.getUsername()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TaskResponse> updateTask(@PathVariable Long id,
                                                   @Valid @RequestBody TaskRequest request,
                                                   @AuthenticationPrincipal UserDetails userDetails) {
        boolean isAdmin = userDetails.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        return ResponseEntity.ok(taskService.updateTask(id, request, userDetails.getUsername(), isAdmin));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id,
                                           @AuthenticationPrincipal UserDetails userDetails) {
        boolean isAdmin = userDetails.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        taskService.deleteTask(id, userDetails.getUsername(), isAdmin);
        return ResponseEntity.noContent().build();
    }
}
