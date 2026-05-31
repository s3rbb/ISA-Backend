package taskmanager_isa.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ProjectResponse {

    private Long id;
    private String name;
    private String description;
    private Long ownerId;
    private String ownerUsername;
}
