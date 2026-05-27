package com.epam.ecommerce.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "products")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer productId;

    @Column(nullable = false, length=100)
    private String productName;

    @Column(nullable = false, length=100)
    private String productCategory;

    @Column(nullable = false, length=100)
    private Double productPrice;

    @Column(nullable = false)
    private boolean deleted = false;
}
