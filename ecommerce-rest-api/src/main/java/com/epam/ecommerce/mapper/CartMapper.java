package com.epam.ecommerce.mapper;

import com.epam.ecommerce.dto.*;
import com.epam.ecommerce.model.Cart;
import com.epam.ecommerce.model.CartItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CartMapper {

    @Mapping(source = "user.userId", target = "userId")
    @Mapping(source = "cartItems", target = "items")
    @Mapping(target = "totalPrice", expression = "java(calculateTotal(cart))")
    CartResponseDTO toResponseDTO(Cart cart);

    @Mapping(source = "product.productName", target = "productName")
    @Mapping(source = "productQuantity", target = "quantity")
    @Mapping(source = "totalPrice", target = "totalPrice")
    CartItemResponseDTO toItemResponseDTO(CartItem cartItem);

    List<CartItemResponseDTO> toItemResponseDTOs(List<CartItem> items);

    default Double calculateTotal(Cart cart) {
        if (cart.getCartItems() == null) return 0.0;

        return cart.getCartItems().stream()
                .mapToDouble(CartItem::getTotalPrice)
                .sum();
    }
}
