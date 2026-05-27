package com.epam.ecommerce.controller;

import com.epam.ecommerce.dto.ProductResponseDTO;
import com.epam.ecommerce.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserProductController.class)
class UserProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    private ProductResponseDTO response;

    @BeforeEach
    void setUp() {

        response = new ProductResponseDTO();
        response.setProductId(1);
        response.setProductName("Laptop");
        response.setProductCategory("Electronics");
        response.setProductPrice(50000.0);
    }

    @Test
    void getAllProducts_shouldReturnOk() throws Exception {
        when(productService.getAllActiveProducts())
                .thenReturn(List.of(response));

        mockMvc.perform(get("/api/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].productName").value("Laptop"));

        verify(productService, times(1)).getAllActiveProducts();
    }

    @Test
    void searchProductByName_validRequest_shouldReturnOk() throws Exception {
        when(productService.searchProductsByName("Laptop"))
                .thenReturn(List.of(response));

        mockMvc.perform(get("/api/products/search/name")
                        .param("name", "Laptop"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].productName").value("Laptop"));

        verify(productService, times(1))
                .searchProductsByName("Laptop");
    }

    @Test
    void searchProductByCategory_validRequest_shouldReturnOk() throws Exception {
        when(productService.searchProductsByCategory("Electronics"))
                .thenReturn(List.of(response));

        mockMvc.perform(get("/api/products/search/category")
                        .param("category", "Electronics"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].productCategory").value("Electronics"));

        verify(productService, times(1))
                .searchProductsByCategory("Electronics");
    }

    @Test
    void searchProductByPriceRange_validRequest_shouldReturnOk() throws Exception {
        when(productService.searchProductsByPriceRange(1000.0, 60000.0))
                .thenReturn(List.of(response));

        mockMvc.perform(get("/api/products/search/price")
                        .param("minPrice", "1000")
                        .param("maxPrice", "60000"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].productPrice").value(50000.0));

        verify(productService, times(1))
                .searchProductsByPriceRange(1000.0, 60000.0);
    }
}
