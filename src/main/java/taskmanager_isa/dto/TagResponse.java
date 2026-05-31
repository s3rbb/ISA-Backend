package taskmanager_isa.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class TagResponse {

    private Long id;
    private String name;
    private Long createdById;
    private String createdByUsername;
}
