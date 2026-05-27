package com.epam.ecommerce.controller;

import com.epam.ecommerce.dto.OrderRequestDTO;
import com.epam.ecommerce.dto.OrderResponseDTO;
import com.epam.ecommerce.model.Cart;
import com.epam.ecommerce.service.CartService;
import com.epam.ecommerce.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;
    private final CartService cartService;

    public OrderController(OrderService orderService, CartService cartService) {
        this.orderService = orderService;
        this.cartService = cartService;
    }

    /**
     * Place an order from the user's cart
     */
    @PostMapping("/place")
    public ResponseEntity<OrderResponseDTO> placeOrder(@RequestBody OrderRequestDTO request) {

        Cart cart = cartService.getCartByUserId(request.getUserId());

        OrderResponseDTO orderDTO =
                orderService.placeOrderFromCart(cart, request.getShippingAddress());

        return new ResponseEntity<>(orderDTO, HttpStatus.CREATED);
    }

    /**
     * Get order history for a user
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<OrderResponseDTO>> getUserOrderHistory(@PathVariable Long userId) {
        List<OrderResponseDTO> orders = orderService.getOrderHistoryForUser(userId);
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }

    /**
     * Get all orders
     */
    @GetMapping
    public ResponseEntity<List<OrderResponseDTO>> getAllOrders() {
        List<OrderResponseDTO> orders = orderService.getAllOrders();
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }
}