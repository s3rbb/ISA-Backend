package taskmanager_isa.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import taskmanager_isa.dto.CommentRequest;
import taskmanager_isa.dto.CommentResponse;
import taskmanager_isa.service.CommentService;

import java.util.List;

@RestController
@RequestMapping("/api/tasks/{taskId}/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @GetMapping
    public ResponseEntity<List<CommentResponse>> getComments(@PathVariable Long taskId) {
        return ResponseEntity.ok(commentService.getCommentsByTask(taskId));
    }

    @PostMapping
    public ResponseEntity<CommentResponse> addComment(@PathVariable Long taskId,
                                                      @Valid @RequestBody CommentRequest request,
                                                      @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(commentService.addComment(taskId, request, userDetails.getUsername()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long taskId,
                                              @PathVariable Long id,
                                              @AuthenticationPrincipal UserDetails userDetails) {
        boolean isAdmin = userDetails.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        commentService.deleteComment(id, userDetails.getUsername(), isAdmin);
        return ResponseEntity.noContent().build();
    }
}
