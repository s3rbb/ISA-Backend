package taskmanager_isa.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import taskmanager_isa.dto.UserResponse;
import taskmanager_isa.entity.User;
import taskmanager_isa.repository.UserRepository;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserRepository userRepository;

    @GetMapping
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        List<UserResponse> users = userRepository.findAll().stream()
                .map(user -> new UserResponse(user.getId(), user.getUsername(), user.getEmail(), user.getRole().name()))
                .toList();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return ResponseEntity.ok(new UserResponse(user.getId(), user.getUsername(), user.getEmail(), user.getRole().name()));
    }
}
