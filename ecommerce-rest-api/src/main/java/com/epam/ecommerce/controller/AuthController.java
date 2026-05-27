package com.epam.ecommerce.controller;

import com.epam.ecommerce.dto.UserRequestDTO;
import com.epam.ecommerce.dto.UserResponseDTO;
import com.epam.ecommerce.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService service;

    public AuthController(UserService service) {
        this.service = service;
    }

    // REGISTER
    @PostMapping("/register")
    public ResponseEntity<UserResponseDTO> registerUser(
            @Valid @RequestBody UserRequestDTO dto) {

        UserResponseDTO response = service.registerUser(dto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // LOGIN
    @PostMapping("/login")
    public ResponseEntity<UserResponseDTO> login(
            @RequestParam String email,
            @RequestParam String password) {

        return ResponseEntity.ok(service.login(email, password));
    }
}