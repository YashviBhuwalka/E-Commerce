package com.epam.ecommerce.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "order_items")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String productName; // storing snapshot of product name

    @Column(nullable = false)
    private double productPrice; // storing snapshot of product price

    @Column(nullable = false)
    private int quantity;

    @Column(nullable = false)
    private double totalPrice;

    // Constructor for creating from Product entity
    public OrderItem(Product product, int quantity) {
        this.productName = product.getProductName();
        this.productPrice = product.getProductPrice();
        this.quantity = quantity;
        calculateTotalPrice();
    }

    public void calculateTotalPrice() {
        this.totalPrice = productPrice * quantity;
    }
}