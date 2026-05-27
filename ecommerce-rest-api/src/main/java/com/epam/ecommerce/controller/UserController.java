package com.epam.ecommerce.controller;

import com.epam.ecommerce.dto.UserRequestDTO;
import com.epam.ecommerce.dto.UserResponseDTO;
import com.epam.ecommerce.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    // View own profile
    @GetMapping("/me")
    public ResponseEntity<UserResponseDTO> getMyProfile(@RequestParam Long userId) {
        return ResponseEntity.ok(service.getActiveUserById(userId));
    }

    // Update own profile
    @PutMapping("/me")
    public ResponseEntity<UserResponseDTO> updateMyProfile(
            @RequestParam Long userId,
            @Valid @RequestBody UserRequestDTO dto) {

        return ResponseEntity.ok(service.updateUser(userId, dto));
    }

    // Delete own account
    @DeleteMapping("/me")
    public ResponseEntity<Void> deleteMyAccount(@RequestParam Long userId) {
        service.deleteUser(userId);
        return ResponseEntity.noContent().build();
    }

}