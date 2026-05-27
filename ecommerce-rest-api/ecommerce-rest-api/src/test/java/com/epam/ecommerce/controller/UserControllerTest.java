package com.epam.ecommerce.controller;

import com.epam.ecommerce.dto.UserRequestDTO;
import com.epam.ecommerce.dto.UserResponseDTO;
import com.epam.ecommerce.exceptions.ResourceNotFoundException;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @MockBean
    private UserService service;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    private UserResponseDTO responseDTO;
    private UserRequestDTO requestDTO;

    @BeforeEach
    void setUp() {

        responseDTO = new UserResponseDTO();
        responseDTO.setUsername("John");
        responseDTO.setEmail("john@epam.com");
        responseDTO.setRole(Role.USER);

        requestDTO = new UserRequestDTO();
        requestDTO.setUsername("Johnny");
        requestDTO.setEmail("johnny@epam.com");
        requestDTO.setPassword("password123");
        requestDTO.setRole(Role.USER);
    }

    @Test
    void getMyProfile_authenticatedUser_shouldReturnOk() throws Exception {
        when(service.getUserById(1L)).thenReturn(responseDTO);

        mockMvc.perform(get("/users/me")
                        .param("userId", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("John"));

        verify(service, times(1)).getUserById(1L);
    }

    @Test
    void getMyProfile_shouldReturnNotFound() throws Exception {
        when(service.getUserById(2L)).thenThrow(new ResourceNotFoundException("User not found"));

        mockMvc.perform(get("/users/me")
                        .param("userId", "2"))
                .andExpect(status().isNotFound());

        verify(service, times(1)).getUserById(2L);
    }

    @Test
    void updateMyProfile_validRequest_shouldReturnOk() throws Exception {
        when(service.updateUser(eq(1L), any(UserRequestDTO.class))).thenReturn(responseDTO);

        mockMvc.perform(put("/users/me")
                        .param("userId", "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(requestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("John"))
                .andExpect(jsonPath("$.email").value("john@epam.com"))
                .andExpect(jsonPath("$.role").value("USER"));

        verify(service, times(1)).updateUser(eq(1L), any(UserRequestDTO.class));
    }

    @Test
    void updateMyProfile_invalidRequest_shouldReturnBadRequest() throws Exception {
        UserRequestDTO invalidDTO = new UserRequestDTO();

        mockMvc.perform(put("/users/me")
                        .param("userId", "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(invalidDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deleteMyAccount_authenticatedUser_shouldReturnNoContent() throws Exception {
        doNothing().when(service).deleteUser(1L);

        mockMvc.perform(delete("/users/me")
                        .param("userId", "1"))
                .andExpect(status().isNoContent());

        verify(service, times(1)).deleteUser(1L);
    }

}