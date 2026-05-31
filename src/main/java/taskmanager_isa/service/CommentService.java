package taskmanager_isa.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import taskmanager_isa.dto.CommentRequest;
import taskmanager_isa.dto.CommentResponse;
import taskmanager_isa.entity.Comment;
import taskmanager_isa.entity.Task;
import taskmanager_isa.entity.User;
import taskmanager_isa.exception.ResourceNotFoundException;
import taskmanager_isa.repository.CommentRepository;
import taskmanager_isa.repository.TaskRepository;
import taskmanager_isa.repository.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    public List<CommentResponse> getCommentsByTask(Long taskId) {
        return commentRepository.findByTaskId(taskId).stream()
                .map(this::toResponse)
                .toList();
    }

    public CommentResponse addComment(Long taskId, CommentRequest request, String username) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found"));

        User author = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Comment comment = Comment.builder()
                .text(request.getText())
                .task(task)
                .author(author)
                .build();

        return toResponse(commentRepository.save(comment));
    }

    public void deleteComment(Long id, String username, boolean isAdmin) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Comment not found"));

        if (!isAdmin && !comment.getAuthor().getUsername().equals(username)) {
            throw new RuntimeException("You can only delete your own comments");
        }

        commentRepository.delete(comment);
    }

    private CommentResponse toResponse(Comment comment) {
        return new CommentResponse(
                comment.getId(),
                comment.getText(),
                comment.getCreatedAt(),
                comment.getTask().getId(),
                comment.getAuthor().getId(),
                comment.getAuthor().getUsername()
        );
    }
}
