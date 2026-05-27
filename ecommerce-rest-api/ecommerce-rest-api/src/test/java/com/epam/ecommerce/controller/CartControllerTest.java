package com.epam.ecommerce.controller;

import com.epam.ecommerce.dto.CartItemRequestDTO;
import com.epam.ecommerce.dto.CartRequestDTO;
import com.epam.ecommerce.dto.CartResponseDTO;
import com.epam.ecommerce.service.CartService;
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

@WebMvcTest(CartController.class)
class CartControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CartService cartService;

    @Autowired
    private ObjectMapper objectMapper;

    private CartRequestDTO cartRequestDTO;
    private CartItemRequestDTO itemRequestDTO;
    private CartResponseDTO cartResponseDTO;

    @BeforeEach
    void setUp() {

        itemRequestDTO = new CartItemRequestDTO();
        itemRequestDTO.setProductName("Laptop");
        itemRequestDTO.setQuantity(2);

        cartRequestDTO = new CartRequestDTO();
        cartRequestDTO.setItems(List.of(itemRequestDTO));

        cartResponseDTO = new CartResponseDTO();
        cartResponseDTO.setTotalPrice(100000.0);
    }

    @Test
    void addItemToCart_validRequest_shouldReturnCreated() throws Exception {

        when(cartService.getCartDTO(1L)).thenReturn(cartResponseDTO);

        mockMvc.perform(post("/api/carts/1/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cartRequestDTO)))
                .andExpect(status().isCreated());

        verify(cartService, times(1))
                .addItemToCart(1L, "Laptop", 2);

        verify(cartService, times(1))
                .getCartDTO(1L);
    }

    @Test
    void removeItemFromCart_validRequest_shouldReturnOk() throws Exception {

        when(cartService.getCartDTO(1L)).thenReturn(cartResponseDTO);

        mockMvc.perform(delete("/api/carts/1/items/Laptop"))
                .andExpect(status().isOk());

        verify(cartService, times(1))
                .removeItemFromCart(1L, "Laptop");

        verify(cartService, times(1))
                .getCartDTO(1L);
    }

    @Test
    void viewCart_validId_shouldReturnOk() throws Exception {

        when(cartService.getCartDTO(1L)).thenReturn(cartResponseDTO);

        mockMvc.perform(get("/api/carts/1"))
                .andExpect(status().isOk());

        verify(cartService, times(1)).getCartDTO(1L);
    }

    @Test
    void clearCart_validId_shouldReturnNoContentFound() throws Exception {

        mockMvc.perform(delete("/api/carts/1/clear"))
                .andExpect(status().isNoContent());

        verify(cartService, times(1)).clearCart(1L);
    }
}
