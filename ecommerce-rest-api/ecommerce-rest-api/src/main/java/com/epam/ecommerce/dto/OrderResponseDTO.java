package com.epam.ecommerce.dto;

import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderResponseDTO {

    private Integer orderId;
    private Long userId;
    private List<OrderItemResponseDTO> items;
    private double totalAmount;
    private LocalDateTime orderDate;

}
