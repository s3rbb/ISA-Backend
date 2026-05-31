package taskmanager_isa.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TagRequest {

    @NotBlank
    private String name;
}
