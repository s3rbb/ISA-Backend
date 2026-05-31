package taskmanager_isa.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import taskmanager_isa.dto.TagRequest;
import taskmanager_isa.dto.TagResponse;
import taskmanager_isa.service.TagService;

import java.util.List;

@RestController
@RequestMapping("/api/tags")
@RequiredArgsConstructor
public class TagController {

    private final TagService tagService;

    @GetMapping
    public ResponseEntity<List<TagResponse>> getAllTags() {
        return ResponseEntity.ok(tagService.getAllTags());
    }

    @PostMapping
    public ResponseEntity<TagResponse> createTag(@Valid @RequestBody TagRequest request,
                                                 @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(tagService.createTag(request, userDetails.getUsername()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TagResponse> updateTag(@PathVariable Long id,
                                                 @Valid @RequestBody TagRequest request,
                                                 @AuthenticationPrincipal UserDetails userDetails) {
        boolean isAdmin = userDetails.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        return ResponseEntity.ok(tagService.updateTag(id, request, userDetails.getUsername(), isAdmin));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTag(@PathVariable Long id) {
        tagService.deleteTag(id);
        return ResponseEntity.noContent().build();
    }
}
