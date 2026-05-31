package taskmanager_isa.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
public class TaskResponse {

    private Long id;
    private String title;
    private String status;
    private Long projectId;
    private String projectName;
    private Long createdById;
    private String createdByUsername;
    private Set<TagResponse> tags;
}
