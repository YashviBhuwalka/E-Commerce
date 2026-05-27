package com.epam.ecommerce.service;

import com.epam.ecommerce.dto.OrderResponseDTO;
import com.epam.ecommerce.model.Cart;
import com.epam.ecommerce.model.Order;
import com.epam.ecommerce.model.OrderItem;
import com.epam.ecommerce.repository.OrderRepository;
import com.epam.ecommerce.exceptions.InvalidOperationException;
import com.epam.ecommerce.exceptions.ResourceNotFoundException;
import com.epam.ecommerce.mapper.OrderMapper;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;

    public OrderService(OrderRepository orderRepository, OrderMapper orderMapper) {
        this.orderRepository = orderRepository;
        this.orderMapper = orderMapper;
    }

    /**
     * Place an order based on the user's cart
     */
    public OrderResponseDTO placeOrderFromCart(Cart cart, String shippingAddress) {
        if (cart == null) {
            throw new ResourceNotFoundException("Cart not found");
        }

        if (cart.getUser().isDeleted()) {
            throw new InvalidOperationException("Cannot place order: User account is deleted");
        }

        if (cart.getCartItems().isEmpty()) {
            throw new InvalidOperationException("Cannot place order: Cart is empty for user "
                    + cart.getUser().getUsername());
        }

        // Convert cart items → order items
        List<OrderItem> orderItems = cart.getCartItems().stream()
                .map(item -> {
                    if (item.getProduct().isDeleted()) {
                        throw new InvalidOperationException(
                                "Product '" + item.getProduct().getProductName()
                                        + "' is no longer available");
                    }
                    return new OrderItem(item.getProduct(), item.getProductQuantity());
                })
                .collect(Collectors.toList());

        // Create Order
        Order order = new Order(cart.getUser(),orderItems,shippingAddress);
        orderRepository.save(order);

        // Clear cart
        cart.getCartItems().clear();

        // Map to response DTO using MapStruct
        return orderMapper.toResponseDTO(order);
    }

    /**
     * Get order history for a single user
     */
    public List<OrderResponseDTO> getOrderHistoryForUser(Long userId) {
        List<Order> orders = orderRepository.findOrderByUser_UserIdAndUser_DeletedFalse(userId);
        if (orders == null || orders.isEmpty()) {
            throw new ResourceNotFoundException("No orders found for user id: " + userId);
        }

        return orders.stream()
                .map(orderMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * Get all orders in the system
     */
    public List<OrderResponseDTO> getAllOrders() {
        List<Order> orders = orderRepository.findAll();
        if (orders.isEmpty()) {
            throw new ResourceNotFoundException("No orders found in the system.");
        }

        return orders.stream()
                .map(orderMapper::toResponseDTO)
                .collect(Collectors.toList());
    }
}