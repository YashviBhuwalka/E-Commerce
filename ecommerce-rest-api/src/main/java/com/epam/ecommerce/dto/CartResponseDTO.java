package com.epam.ecommerce.dto;

import lombok.*;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class CartResponseDTO {

    private Long userId;
    private List<CartItemResponseDTO> items;
    private Double totalPrice;
}