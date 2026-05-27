package com.epam.ecommerce.mapper;

import com.epam.ecommerce.dto.OrderItemResponseDTO;
import com.epam.ecommerce.dto.OrderResponseDTO;
import com.epam.ecommerce.model.Order;
import com.epam.ecommerce.model.OrderItem;
import com.epam.ecommerce.model.User;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-05-27T12:49:28+0530",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 21.0.9 (Oracle Corporation)"
)
@Component
public class OrderMapperImpl implements OrderMapper {

    @Override
    public OrderResponseDTO toResponseDTO(Order order) {
        if ( order == null ) {
            return null;
        }

        OrderResponseDTO.OrderResponseDTOBuilder orderResponseDTO = OrderResponseDTO.builder();

        orderResponseDTO.userId( orderUserUserId( order ) );
        orderResponseDTO.items( orderItemListToOrderItemResponseDTOList( order.getOrderItems() ) );
        orderResponseDTO.totalAmount( order.getTotalAmountOfOrder() );
        orderResponseDTO.orderId( order.getOrderId() );
        orderResponseDTO.orderDate( order.getOrderDate() );

        return orderResponseDTO.build();
    }

    @Override
    public OrderItemResponseDTO toOrderItemResponseDTO(OrderItem orderItem) {
        if ( orderItem == null ) {
            return null;
        }

        OrderItemResponseDTO.OrderItemResponseDTOBuilder orderItemResponseDTO = OrderItemResponseDTO.builder();

        orderItemResponseDTO.productName( orderItem.getProductName() );
        orderItemResponseDTO.quantity( orderItem.getQuantity() );
        orderItemResponseDTO.totalPrice( orderItem.getTotalPrice() );

        return orderItemResponseDTO.build();
    }

    private Long orderUserUserId(Order order) {
        if ( order == null ) {
            return null;
        }
        User user = order.getUser();
        if ( user == null ) {
            return null;
        }
        Long userId = user.getUserId();
        if ( userId == null ) {
            return null;
        }
        return userId;
    }

    protected List<OrderItemResponseDTO> orderItemListToOrderItemResponseDTOList(List<OrderItem> list) {
        if ( list == null ) {
            return null;
        }

        List<OrderItemResponseDTO> list1 = new ArrayList<OrderItemResponseDTO>( list.size() );
        for ( OrderItem orderItem : list ) {
            list1.add( toOrderItemResponseDTO( orderItem ) );
        }

        return list1;
    }
}
