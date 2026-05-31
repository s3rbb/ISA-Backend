package taskmanager_isa.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class CommentResponse {

    private Long id;
    private String text;
    private LocalDateTime createdAt;
    private Long taskId;
    private Long authorId;
    private String authorUsername;
}
