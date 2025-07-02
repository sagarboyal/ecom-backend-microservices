package com.main.userservice.controller;
import com.main.userservice.constraints.Constraints;
import com.main.userservice.payload.request.UserRequest;
import com.main.userservice.payload.response.PagedResponse;
import com.main.userservice.payload.response.UserResponse;
import com.main.userservice.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<UserResponse> createUser(@Valid @RequestBody UserRequest userRequest) {
        return ResponseEntity.ok(userService.createUser(userRequest));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @GetMapping("/{email}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable String email) {
        return ResponseEntity.ok(userService.getUserByEmailId(email));
    }

    @GetMapping
    public ResponseEntity<PagedResponse<UserResponse>> getAllUsers(
            @RequestParam(required = false, defaultValue = Constraints.PAGE_NUMBER) Integer pageNumber,
            @RequestParam(required = false, defaultValue = Constraints.PAGE_SIZE) Integer pageSize
    ) {
        return ResponseEntity.ok(userService.getAllUser(pageNumber, pageSize));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponse> updateUser(@PathVariable Long id, @Valid @RequestBody UserRequest request) {
        return ResponseEntity.ok(userService.updateUser(id, request));
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok("User deleted successfully.");
    }
}

