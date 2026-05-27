package com.epam.ecommerce.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class CartItemResponseDTO {
    private String productName;
    private Integer quantity;
    private Double totalPrice;
}