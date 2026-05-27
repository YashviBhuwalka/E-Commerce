package com.epam.ecommerce.service;

import com.epam.ecommerce.dto.OrderResponseDTO;
import com.epam.ecommerce.exceptions.InvalidOperationException;
import com.epam.ecommerce.exceptions.ResourceNotFoundException;
import com.epam.ecommerce.mapper.OrderMapper;
import com.epam.ecommerce.model.*;
import com.epam.ecommerce.repository.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderMapper orderMapper;

    @InjectMocks
    private OrderService orderService;

    private User user;
    private Product product;
    private Cart cart;
    private CartItem cartItem;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setUserId(1L);
        user.setUsername("yashvi");
        user.setDeleted(false);

        product = new Product();
        product.setProductName("Laptop");
        product.setProductPrice(50000.0);
        product.setDeleted(false);

        cartItem = new CartItem();
        cartItem.setProduct(product);
        cartItem.setProductQuantity(2);

        cart = new Cart();
        cart.setUser(user);
        cart.setCartItems(new ArrayList<>(List.of(cartItem)));
    }

    @Test
    void placeOrderFromCart_validCart_shouldCreateOrder() {
        OrderResponseDTO responseDTO = new OrderResponseDTO();

        when(orderMapper.toResponseDTO(any(Order.class)))
                .thenReturn(responseDTO);

        OrderResponseDTO result =
                orderService.placeOrderFromCart(cart, "Bangalore");

        assertNotNull(result);
        verify(orderRepository, times(1)).save(any(Order.class));
        verify(orderMapper, times(1)).toResponseDTO(any(Order.class));
        assertTrue(cart.getCartItems().isEmpty());
    }

    @Test
    void placeOrderFromCart_CartNull_ShouldThrowException() {
        assertThrows(ResourceNotFoundException.class,
                () -> orderService.placeOrderFromCart(null, "Address"));
    }

    @Test
    void placeOrderFromCart_UserDeleted_ShouldThrowException() {
        user.setDeleted(true);

        assertThrows(InvalidOperationException.class,
                () -> orderService.placeOrderFromCart(cart, "Address"));
    }

    @Test
    void placeOrderFromCart_EmptyCart_ShouldThrowException() {
        cart.setCartItems(new ArrayList<>());

        assertThrows(InvalidOperationException.class,
                () -> orderService.placeOrderFromCart(cart, "Address"));
    }

    @Test
    void placeOrderFromCart_ProductDeleted_ShouldThrowException() {
        product.setDeleted(true);

        assertThrows(InvalidOperationException.class,
                () -> orderService.placeOrderFromCart(cart, "Address"));
    }

    @Test
    void getOrderHistoryForUser_existingUser_shouldReturnOrders() {
        Order order = new Order();
        List<Order> orders = List.of(order);

        when(orderRepository
                .findOrderByUser_UserIdAndUser_DeletedFalse(1L))
                .thenReturn(orders);

        when(orderMapper.toResponseDTO(order))
                .thenReturn(new OrderResponseDTO());

        List<OrderResponseDTO> result =
                orderService.getOrderHistoryForUser(1L);

        assertEquals(1, result.size());
    }

    @Test
    void getOrderHistoryForUser_NoOrders_ShouldThrowException() {
        when(orderRepository
                .findOrderByUser_UserIdAndUser_DeletedFalse(1L))
                .thenReturn(new ArrayList<>());

        assertThrows(ResourceNotFoundException.class,
                () -> orderService.getOrderHistoryForUser(1L));
    }

    @Test
    void getAllOrders_ordersExist_shouldReturnAllOrders() {
        Order order = new Order();

        when(orderRepository.findAll())
                .thenReturn(List.of(order));

        when(orderMapper.toResponseDTO(order))
                .thenReturn(new OrderResponseDTO());

        List<OrderResponseDTO> result = orderService.getAllOrders();

        assertEquals(1, result.size());
    }

    @Test
    void getAllOrders_NoOrders_ShouldThrowException() {
        when(orderRepository.findAll())
                .thenReturn(new ArrayList<>());

        assertThrows(ResourceNotFoundException.class,
                () -> orderService.getAllOrders());
    }
}
