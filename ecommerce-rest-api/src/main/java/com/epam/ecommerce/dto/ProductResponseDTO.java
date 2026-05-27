package com.epam.ecommerce.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductResponseDTO {
    private Integer productId;
    private String productName;
    private String productCategory;
    private Double productPrice;
    private boolean deleted;
}
