package com.epam.ecommerce.service;

import com.epam.ecommerce.dto.UserRequestDTO;
import com.epam.ecommerce.dto.UserResponseDTO;
import com.epam.ecommerce.exceptions.InvalidOperationException;
import com.epam.ecommerce.exceptions.ResourceNotFoundException;
import com.epam.ecommerce.mapper.UserMapper;
import com.epam.ecommerce.model.User;
import com.epam.ecommerce.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository repository;
    private final UserMapper userMapper;

    public UserService(UserRepository repository, UserMapper mapper) {
        this.repository = repository;
        this.userMapper=mapper;
    }

    // USER REGISTRATION
    public UserResponseDTO registerUser(UserRequestDTO dto) {

        Optional<User> existingUser = repository.findByEmail(dto.getEmail());

        if (existingUser.isPresent()) {
            User user = existingUser.get();

            if (user.isDeleted()) {
                // Restore user
                user.setDeleted(false);
                user.setUsername(dto.getUsername());
                user.setPassword(dto.getPassword());
                user.setRole(dto.getRole());

                return userMapper.toResponseDTO(repository.save(user));
            } else {
                throw new InvalidOperationException(
                        "A user with email '" + dto.getEmail() + "' is already registered");
            }
        }

        // New user
        User newUser = userMapper.toEntity(dto);
        newUser.setDeleted(false);
        User savedUser = repository.save(newUser);

        return userMapper.toResponseDTO(savedUser);
    }

    // GET ALL USERS
    public List<UserResponseDTO> getAllUsers() {
        return repository.findAll().stream()
                .map(userMapper::toResponseDTO)
                .toList();
    }

    // GET USER BY ID
    public UserResponseDTO getUserById(Long id) {

        User user = repository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found with id: " + id));

        return userMapper.toResponseDTO(user);
    }

    public UserResponseDTO getActiveUserById(Long id) {

        User user = repository.findByUserIdAndDeletedFalse(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Active user not found with id: " + id));

        return userMapper.toResponseDTO(user);
    }

    // UPDATE USER
    public UserResponseDTO updateUser(Long id, UserRequestDTO dto) {
        User existingUser = repository.findByUserIdAndDeletedFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));

        if (dto.getUsername() != null && !dto.getUsername().isBlank()) {
            existingUser.setUsername(dto.getUsername());
        }
        if (dto.getEmail() != null && !dto.getEmail().isBlank()) {
            existingUser.setEmail(dto.getEmail());
        }
        if (dto.getPassword() != null && !dto.getPassword().isBlank()) {
            existingUser.setPassword(dto.getPassword());
        }
        if (dto.getRole() != null) {
            existingUser.setRole(dto.getRole());
        }

        User updatedUser=repository.save(existingUser);
        return userMapper.toResponseDTO(updatedUser);
    }

    // DELETE USER
    public void deleteUser(Long id) {
        User user = repository.findByUserIdAndDeletedFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
        user.setDeleted(true);
        repository.save(user);
    }

    // LOGIN
    public UserResponseDTO login(String email, String password) {
        User user = repository.findByEmailAndDeletedFalse(email)
                .orElseThrow(() -> new InvalidOperationException("User not found with email: " + email));

        if (!user.getPassword().equals(password)) {
            throw new InvalidOperationException("Invalid password for user: " + email);
        }
        return userMapper.toResponseDTO(user);
    }

}
