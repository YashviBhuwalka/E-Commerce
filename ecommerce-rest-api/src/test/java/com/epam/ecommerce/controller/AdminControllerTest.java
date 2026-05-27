package com.epam.ecommerce.controller;

import com.epam.ecommerce.dto.UserRequestDTO;
import com.epam.ecommerce.dto.UserResponseDTO;
import com.epam.ecommerce.model.Role;
import com.epam.ecommerce.service.UserService;
import com.epam.ecommerce.exceptions.GlobalExceptionHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AdminController.class)
@Import(GlobalExceptionHandler.class)
class AdminControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private UserService service;

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
    void getAllUsers_usersExist_shouldReturnOk() throws Exception {
        when(service.getAllUsers()).thenReturn(List.of(responseDTO));

        mockMvc.perform(get("/admin/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].username").value("John"));

        verify(service, times(1)).getAllUsers();
    }

    @Test
    void getUserById_validId_shouldReturnOk() throws Exception {
        when(service.getUserById(1L)).thenReturn(responseDTO);

        mockMvc.perform(get("/admin/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("John"));

        verify(service, times(1)).getUserById(1L);
    }

    @Test
    void updateUser_validRequest_shouldReturnOk() throws Exception {
        when(service.updateUser(eq(1L), any(UserRequestDTO.class)))
                .thenReturn(responseDTO);

        mockMvc.perform(put("/admin/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(requestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("John"));

        verify(service, times(1))
                .updateUser(eq(1L), any(UserRequestDTO.class));
    }

    @Test
    void deleteUser_validId_shouldReturnNoContent() throws Exception {
        doNothing().when(service).deleteUser(1L);

        mockMvc.perform(delete("/admin/users/1"))
                .andExpect(status().isNoContent());

        verify(service, times(1)).deleteUser(1L);
    }
}
