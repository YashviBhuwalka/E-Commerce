package com.epam.ecommerce.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductRequestDTO {

    @NotBlank(message = "Product name is required")
    @Size(max = 100, message = "Product name must be at most 100 characters")
    private String productName;

    @NotBlank(message = "Product category is required")
    @Size(max = 100, message = "Product category must be at most 100 characters")
    private String productCategory;

    @NotNull(message = "Product price is required")
    @Positive(message = "Product price must be positive")
    private Double productPrice;

}
