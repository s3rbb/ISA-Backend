package taskmanager_isa.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import taskmanager_isa.dto.TagRequest;
import taskmanager_isa.dto.TagResponse;
import taskmanager_isa.entity.Tag;
import taskmanager_isa.entity.User;
import taskmanager_isa.exception.ResourceNotFoundException;
import taskmanager_isa.repository.TagRepository;
import taskmanager_isa.repository.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TagService {

    private final TagRepository tagRepository;
    private final UserRepository userRepository;

    public List<TagResponse> getAllTags() {
        return tagRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    public TagResponse createTag(TagRequest request, String username) {
        User creator = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Tag tag = Tag.builder()
                .name(request.getName())
                .createdBy(creator)
                .build();
        return toResponse(tagRepository.save(tag));
    }

    public TagResponse updateTag(Long id, TagRequest request, String username, boolean isAdmin) {
        Tag tag = tagRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tag not found"));

        if (!isAdmin && (tag.getCreatedBy() == null || !tag.getCreatedBy().getUsername().equals(username))) {
            throw new RuntimeException("You can only edit your own tags");
        }

        tag.setName(request.getName());
        return toResponse(tagRepository.save(tag));
    }

    public void deleteTag(Long id) {
        if (!tagRepository.existsById(id)) {
            throw new ResourceNotFoundException("Tag not found");
        }
        tagRepository.deleteById(id);
    }

    private TagResponse toResponse(Tag tag) {
        return new TagResponse(
                tag.getId(),
                tag.getName(),
                tag.getCreatedBy() != null ? tag.getCreatedBy().getId() : null,
                tag.getCreatedBy() != null ? tag.getCreatedBy().getUsername() : null
        );
    }
}
