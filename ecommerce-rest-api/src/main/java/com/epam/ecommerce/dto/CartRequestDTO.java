package com.epam.ecommerce.dto;

import jakarta.validation.constraints.*;
import lombok.*;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class CartRequestDTO {

    @NotEmpty(message = "Cart items cannot be empty")
    private List<CartItemRequestDTO> items;
}