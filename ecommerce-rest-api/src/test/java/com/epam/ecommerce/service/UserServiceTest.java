package com.epam.ecommerce.service;

import com.epam.ecommerce.dto.UserRequestDTO;
import com.epam.ecommerce.dto.UserResponseDTO;
import com.epam.ecommerce.exceptions.InvalidOperationException;
import com.epam.ecommerce.exceptions.ResourceNotFoundException;
import com.epam.ecommerce.mapper.UserMapper;
import com.epam.ecommerce.model.Role;
import com.epam.ecommerce.model.User;
import com.epam.ecommerce.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.*;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository repository;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserService userService;

    private User user;
    private UserRequestDTO requestDTO;
    private UserResponseDTO responseDTO;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setUserId(1L);
        user.setUsername("John");
        user.setEmail("john@epam.com");
        user.setPassword("john123");
        user.setRole(Role.USER);
        user.setDeleted(false);

        requestDTO = new UserRequestDTO();
        requestDTO.setUsername("John");
        requestDTO.setEmail("john@epam.com");
        requestDTO.setPassword("john123");
        requestDTO.setRole(Role.USER);

        responseDTO = new UserResponseDTO();
        responseDTO.setUsername("John");
        responseDTO.setEmail("john@epam.com");
        responseDTO.setRole(Role.USER);
    }

    @Test
    void registerUser_newUser_shouldReturnSavedUser() {
        when(repository.findByEmail(requestDTO.getEmail())).thenReturn(Optional.empty());
        when(userMapper.toEntity(requestDTO)).thenReturn(user);
        when(repository.save(user)).thenReturn(user);
        when(userMapper.toResponseDTO(user)).thenReturn(responseDTO);

        UserResponseDTO result = userService.registerUser(requestDTO);

        assertNotNull(result);
        assertEquals("John", result.getUsername());
        verify(repository, times(1)).save(user);
    }

    @Test
    void registerUser_existingDeletedUser_shouldReactivateUser() {
        user.setDeleted(true);
        when(repository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        when(repository.save(user)).thenReturn(user);
        when(userMapper.toResponseDTO(user)).thenReturn(responseDTO);

        UserResponseDTO result = userService.registerUser(requestDTO);

        assertFalse(user.isDeleted());
        assertEquals("John", result.getUsername());
        verify(repository, times(1)).save(user);
    }

    @Test
    void registerUser_existingActiveUser_shouldThrowException() {
        when(repository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));

        InvalidOperationException exception = assertThrows(
                InvalidOperationException.class,
                () -> userService.registerUser(requestDTO)
        );

        assertEquals("A user with email 'john@epam.com' is already registered", exception.getMessage());
    }

    @Test
    void getAllUsers_usersExist_shouldReturnUsers() {
        List<User> users = List.of(user);
        when(repository.findByDeletedFalse()).thenReturn(users);
        when(userMapper.toResponseDTO(user)).thenReturn(responseDTO);

        List<UserResponseDTO> result = userService.getAllUsers();

        assertEquals(1, result.size());
        assertEquals("John", result.get(0).getUsername());
    }

    @Test
    void getUserById_existingId_shouldReturnUser() {
        when(repository.findByUserIdAndDeletedFalse(1L)).thenReturn(Optional.of(user));
        when(userMapper.toResponseDTO(user)).thenReturn(responseDTO);

        UserResponseDTO result = userService.getUserById(1L);

        assertNotNull(result);
        assertEquals("John", result.getUsername());
    }

    @Test
    void getUserById_notFound_shouldThrowException() {
        when(repository.findByUserIdAndDeletedFalse(2L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> userService.getUserById(2L));
    }

    @Test
    void updateUser_allFieldsUpdated_shouldReturnUpdatedUser() {
        when(repository.findByUserIdAndDeletedFalse(1L)).thenReturn(Optional.of(user));
        when(repository.save(user)).thenReturn(user);

        UserRequestDTO updateDTO = new UserRequestDTO();
        updateDTO.setUsername("Johnny");
        updateDTO.setEmail("johnny@epam.com");
        updateDTO.setPassword("newpassword123");
        updateDTO.setRole(Role.ADMIN);

        UserResponseDTO response = new UserResponseDTO();
        when(userMapper.toResponseDTO(user)).thenReturn(response);

        userService.updateUser(1L, updateDTO);

        assertEquals("Johnny", user.getUsername());
        assertEquals("johnny@epam.com", user.getEmail());
        assertEquals("newpassword123", user.getPassword());
        assertEquals(Role.ADMIN, user.getRole());

        verify(repository).save(user);
    }

    @Test
    void updateUser_noFieldsUpdated_shouldReturnUser() {
        when(repository.findByUserIdAndDeletedFalse(1L)).thenReturn(Optional.of(user));
        when(repository.save(user)).thenReturn(user);

        UserRequestDTO updateDTO = new UserRequestDTO();
        updateDTO.setUsername(null);
        updateDTO.setEmail(null);
        updateDTO.setPassword(null);
        updateDTO.setRole(null);

        UserResponseDTO response = new UserResponseDTO();
        when(userMapper.toResponseDTO(user)).thenReturn(response);

        userService.updateUser(1L, updateDTO);

        assertEquals("John", user.getUsername());
        assertEquals("john@epam.com", user.getEmail());
        assertEquals("john123", user.getPassword());
        assertEquals(Role.USER, user.getRole());

        verify(repository).save(user);
    }

    @Test
    void updateUser_blankFields_shouldRetainExistingUser() {
        when(repository.findByUserIdAndDeletedFalse(1L)).thenReturn(Optional.of(user));
        when(repository.save(user)).thenReturn(user);

        UserRequestDTO updateDTO = new UserRequestDTO();
        updateDTO.setUsername("  ");
        updateDTO.setEmail("");
        updateDTO.setPassword(" ");
        updateDTO.setRole(null);

        UserResponseDTO response = new UserResponseDTO();
        when(userMapper.toResponseDTO(user)).thenReturn(response);

        userService.updateUser(1L, updateDTO);

        assertEquals("John", user.getUsername());
        assertEquals("john@epam.com", user.getEmail());
        assertEquals("john123", user.getPassword());
        assertEquals(Role.USER, user.getRole());

        verify(repository).save(user);
    }

    @Test
    void updateUser_notFound_shouldThrowException() {
        when(repository.findByUserIdAndDeletedFalse(2L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> userService.updateUser(2L, requestDTO));
    }

    @Test
    void deleteUser_shouldMarkUserAsDeleted() {
        when(repository.findByUserIdAndDeletedFalse(1L)).thenReturn(Optional.of(user));

        userService.deleteUser(1L);

        assertTrue(user.isDeleted());
        verify(repository, times(1)).save(user);
    }

    @Test
    void deleteUser_notFound_shouldThrowException() {
        when(repository.findByUserIdAndDeletedFalse(2L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> userService.deleteUser(2L));
    }

    @Test
    void login_validCredentials_shouldReturnUser() {
        when(repository.findByEmailAndDeletedFalse("john@epam.com")).thenReturn(Optional.of(user));
        when(userMapper.toResponseDTO(user)).thenReturn(responseDTO);

        UserResponseDTO result = userService.login("john@epam.com", "john123");

        assertEquals("John", result.getUsername());
    }

    @Test
    void login_userNotFound_shouldThrowException() {
        when(repository.findByEmailAndDeletedFalse("unknown@epam.com")).thenReturn(Optional.empty());

        assertThrows(InvalidOperationException.class,
                () -> userService.login("unknown@epam.com", "password123"));
    }

    @Test
    void login_invalidPassword_shouldThrowException() {
        when(repository.findByEmailAndDeletedFalse("john@epam.com")).thenReturn(Optional.of(user));

        assertThrows(InvalidOperationException.class,
                () -> userService.login("john@epam.com", "wrongpassword"));
    }
}