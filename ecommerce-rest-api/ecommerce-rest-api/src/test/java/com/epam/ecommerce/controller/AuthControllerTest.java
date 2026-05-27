package com.epam.ecommerce.controller;

import com.epam.ecommerce.dto.UserRequestDTO;
import com.epam.ecommerce.dto.UserResponseDTO;
import com.epam.ecommerce.exceptions.InvalidOperationException;
import com.epam.ecommerce.model.Role;
import com.epam.ecommerce.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthController.class)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService service;

    @Autowired
    private ObjectMapper mapper;

    private UserRequestDTO requestDTO;
    private UserResponseDTO responseDTO;

    @BeforeEach
    void setUp() {

        requestDTO = new UserRequestDTO();
        requestDTO.setUsername("John");
        requestDTO.setEmail("john@epam.com");
        requestDTO.setPassword("john1234");
        requestDTO.setRole(Role.USER);

        responseDTO = new UserResponseDTO();
        responseDTO.setUsername("John");
        responseDTO.setEmail("john@epam.com");
        responseDTO.setRole(Role.USER);
    }

    @Test
    void registerUser_validRequest_shouldReturnCreatedStatus() throws Exception {

        when(service.registerUser(any(UserRequestDTO.class)))
                .thenReturn(responseDTO);

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(requestDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.username").value("John"))
                .andExpect(jsonPath("$.email").value("john@epam.com"))
                .andExpect(jsonPath("$.role").value("USER"));

        verify(service, times(1)).registerUser(any(UserRequestDTO.class));
    }

    @Test
    void registerUser_invalidRequest_shouldReturnBadRequest() throws Exception {

        UserRequestDTO invalidDTO = new UserRequestDTO();

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(invalidDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void login_validCredentials_shouldReturnOk() throws Exception {

        when(service.login("john@epam.com", "john1234"))
                .thenReturn(responseDTO);

        mockMvc.perform(post("/api/auth/login")
                        .param("email", "john@epam.com")
                        .param("password", "john1234"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("John"))
                .andExpect(jsonPath("$.email").value("john@epam.com"))
                .andExpect(jsonPath("$.role").value("USER"));

        verify(service, times(1)).login("john@epam.com", "john1234");
    }

    @Test
    void login_invalidCredentials_shouldReturnBadRequest() throws Exception {

        when(service.login("john@epam.com", "wrongpassword"))
                .thenThrow(new InvalidOperationException("Invalid credentials"));

        mockMvc.perform(post("/api/auth/login")
                        .param("email", "john@epam.com")
                        .param("password", "wrongpassword"))
                .andExpect(status().isBadRequest());
    }
}
