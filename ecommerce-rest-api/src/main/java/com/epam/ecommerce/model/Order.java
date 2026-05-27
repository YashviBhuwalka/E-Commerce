package com.epam.ecommerce.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer orderId;

    // Many orders belong to one user
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer","handler"})
    private User user;

    // One order can have many order items
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "order_id")
    private List<OrderItem> orderItems = new ArrayList<>();

    @Column(nullable = false)
    private String shippingAddress;

    @Column(nullable = false)
    private double totalAmountOfOrder;

    @Column(nullable = false)
    private LocalDateTime orderDate;

    public Order(User user, List<OrderItem> orderItems, String shippingAddress) {
        this.user = user;
        this.orderItems = orderItems;
        this.shippingAddress = shippingAddress;
        this.totalAmountOfOrder = orderItems.stream()
                .mapToDouble(OrderItem::getTotalPrice)
                .sum();
        this.orderDate = LocalDateTime.now();
    }

}