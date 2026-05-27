package com.epam.ecommerce.mapper;

import com.epam.ecommerce.dto.*;
import com.epam.ecommerce.model.Order;
import com.epam.ecommerce.model.OrderItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface OrderMapper {

        @Mapping(source = "user.userId", target = "userId")
        @Mapping(source = "orderItems", target = "items")
        @Mapping(source = "totalAmountOfOrder", target = "totalAmount")
        OrderResponseDTO toResponseDTO(Order order);

        OrderItemResponseDTO toOrderItemResponseDTO(OrderItem orderItem);
}