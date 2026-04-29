package com.example.rentaroapp.controller;

import com.example.rentaroapp.model.User;
import com.example.rentaroapp.model.UserRole;
import com.example.rentaroapp.service.UserService;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody RegisterRequest request) {
        User user = userService.register(
                request.email(),
                request.passwordHash(),
                request.firstName(),
                request.lastName(),
                request.role()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.findById(id));
    }

    @GetMapping
    public ResponseEntity<List<User>> getAll() {
        return ResponseEntity.ok(userService.getAll());
    }

    public record RegisterRequest(
            @NotBlank @Email String email,
            @NotBlank String passwordHash,
            @NotBlank String firstName,
            @NotBlank String lastName,
            UserRole role
    ) {}
}
