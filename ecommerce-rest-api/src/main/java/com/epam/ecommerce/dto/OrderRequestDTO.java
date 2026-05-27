package com.epam.ecommerce.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderRequestDTO {

    @NotBlank(message = "UserId is required")
    private Long userId;

    @NotBlank(message = "Shipping address is required")
    private String shippingAddress;
}