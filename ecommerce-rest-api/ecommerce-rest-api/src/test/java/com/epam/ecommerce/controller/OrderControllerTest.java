package com.epam.ecommerce.controller;

import com.epam.ecommerce.dto.OrderRequestDTO;
import com.epam.ecommerce.dto.OrderResponseDTO;
import com.epam.ecommerce.model.Cart;
import com.epam.ecommerce.service.CartService;
import com.epam.ecommerce.service.OrderService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(OrderController.class)
class OrderControllerTest {

    @MockBean
    private OrderService orderService;

    @MockBean
    private CartService cartService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private OrderRequestDTO request;
    private OrderResponseDTO response;
    private Cart cart;

    @BeforeEach
    void setUp() {

        request = new OrderRequestDTO();
        request.setUserId(100L);
        request.setShippingAddress("Bangalore");

        response = new OrderResponseDTO();
        response.setOrderId(1);
        response.setUserId(100L);
        response.setTotalAmount(50000.0);
        response.setOrderDate(LocalDateTime.now());
        response.setItems(new ArrayList<>());

        cart = new Cart();
    }

    @Test
    void placeOrder_validRequest_shouldReturnCreated() throws Exception {

        when(cartService.getCartByUserId(100L)).thenReturn(cart);
        when(orderService.placeOrderFromCart(cart, "Bangalore"))
                .thenReturn(response);

        mockMvc.perform(post("/api/orders/place")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.orderId").value(1))
                .andExpect(jsonPath("$.userId").value(100L))
                .andExpect(jsonPath("$.totalAmount").value(50000.0));
    }

    @Test
    void getUserOrderHistory_authenticatedUser_shouldReturnOk() throws Exception {

        when(orderService.getOrderHistoryForUser(100L))
                .thenReturn(List.of(response));

        mockMvc.perform(get("/api/orders/user/100"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].orderId").value(1));
    }

    @Test
    void getAllOrders_shouldReturnOk() throws Exception {

        when(orderService.getAllOrders())
                .thenReturn(List.of(response));

        mockMvc.perform(get("/api/orders"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].orderId").value(1));
    }
}
