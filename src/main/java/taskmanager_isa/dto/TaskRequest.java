package taskmanager_isa.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class TaskRequest {

    @NotBlank
    private String title;

    @NotNull
    private String status;

    @NotNull
    private Long projectId;

    private Set<Long> tagIds;
}
