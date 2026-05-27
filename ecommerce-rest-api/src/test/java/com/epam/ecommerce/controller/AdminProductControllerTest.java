package com.epam.ecommerce.controller;

import com.epam.ecommerce.dto.ProductRequestDTO;
import com.epam.ecommerce.dto.ProductResponseDTO;
import com.epam.ecommerce.service.ProductService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AdminProductController.class)
class AdminProductControllerTest {

    @MockBean
    private ProductService productService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private ProductRequestDTO request;
    private ProductResponseDTO response;

    @BeforeEach
    void setUp() {

        request = new ProductRequestDTO();
        request.setProductName("Laptop");
        request.setProductCategory("Electronics");
        request.setProductPrice(50000.0);

        response = new ProductResponseDTO();
        response.setProductId(1);
        response.setProductName("Laptop");
        response.setProductCategory("Electronics");
        response.setProductPrice(50000.0);
    }

    @Test
    void addProduct_validRequest_shouldReturnCreated() throws Exception {

        ProductRequestDTO requestDTO = new ProductRequestDTO();
        requestDTO.setProductName("Laptop");
        requestDTO.setProductCategory("Electronics");
        requestDTO.setProductPrice(50000.0);

        ProductResponseDTO responseDTO = new ProductResponseDTO();
        responseDTO.setProductName("Laptop");
        responseDTO.setProductCategory("Electronics");
        responseDTO.setProductPrice(50000.0);

        when(productService.addProduct(any(ProductRequestDTO.class)))
                .thenReturn(responseDTO);

        mockMvc.perform(post("/api/admin/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.productName").value("Laptop"));

        verify(productService, times(1)).addProduct(any(ProductRequestDTO.class));
    }

    @Test
    void addProduct_invalidRequest_shouldReturnBadRequest() throws Exception {

        ProductRequestDTO invalidDTO = new ProductRequestDTO(); // empty fields

        mockMvc.perform(post("/api/admin/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getAllProducts_shouldReturnOk() throws Exception {

        ProductResponseDTO responseDTO = new ProductResponseDTO();
        responseDTO.setProductName("Laptop");
        responseDTO.setProductCategory("Electronics");
        responseDTO.setProductPrice(50000.0);

        when(productService.getAllProducts())
                .thenReturn(List.of(responseDTO));

        mockMvc.perform(get("/api/admin/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].productName").value("Laptop"));

        verify(productService, times(1)).getAllProducts();
    }

    @Test
    void getProductById_validId_shouldReturnOk() throws Exception {

        ProductResponseDTO responseDTO = new ProductResponseDTO();
        responseDTO.setProductName("Laptop");

        when(productService.getProductById(1))
                .thenReturn(responseDTO);

        mockMvc.perform(get("/api/admin/products/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.productName").value("Laptop"));

        verify(productService, times(1)).getProductById(1);
    }

    @Test
    void updateProduct_validRequest_shouldReturnOk() throws Exception {

        ProductRequestDTO requestDTO = new ProductRequestDTO();
        requestDTO.setProductName("Updated Laptop");
        requestDTO.setProductCategory("Electronics");
        requestDTO.setProductPrice(60000.0);

        ProductResponseDTO responseDTO = new ProductResponseDTO();
        responseDTO.setProductName("Updated Laptop");

        when(productService.updateProduct(eq(1), any(ProductRequestDTO.class)))
                .thenReturn(responseDTO);

        mockMvc.perform(put("/api/admin/products/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.productName").value("Updated Laptop"));

        verify(productService, times(1))
                .updateProduct(eq(1), any(ProductRequestDTO.class));
    }

    @Test
    void deleteProduct_validId_shouldReturnNoContent() throws Exception {

        doNothing().when(productService).deleteProduct(1);

        mockMvc.perform(delete("/api/admin/products/1"))
                .andExpect(status().isNoContent());

        verify(productService, times(1)).deleteProduct(1);
    }
}
