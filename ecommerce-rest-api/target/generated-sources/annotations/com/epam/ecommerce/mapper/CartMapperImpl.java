package com.epam.ecommerce.mapper;

import com.epam.ecommerce.dto.CartItemResponseDTO;
import com.epam.ecommerce.dto.CartResponseDTO;
import com.epam.ecommerce.model.Cart;
import com.epam.ecommerce.model.CartItem;
import com.epam.ecommerce.model.Product;
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
public class CartMapperImpl implements CartMapper {

    @Override
    public CartResponseDTO toResponseDTO(Cart cart) {
        if ( cart == null ) {
            return null;
        }

        CartResponseDTO.CartResponseDTOBuilder cartResponseDTO = CartResponseDTO.builder();

        cartResponseDTO.userId( cartUserUserId( cart ) );
        cartResponseDTO.items( toItemResponseDTOs( cart.getCartItems() ) );

        cartResponseDTO.totalPrice( calculateTotal(cart) );

        return cartResponseDTO.build();
    }

    @Override
    public CartItemResponseDTO toItemResponseDTO(CartItem cartItem) {
        if ( cartItem == null ) {
            return null;
        }

        CartItemResponseDTO.CartItemResponseDTOBuilder cartItemResponseDTO = CartItemResponseDTO.builder();

        cartItemResponseDTO.productName( cartItemProductProductName( cartItem ) );
        cartItemResponseDTO.quantity( cartItem.getProductQuantity() );
        cartItemResponseDTO.totalPrice( cartItem.getTotalPrice() );

        return cartItemResponseDTO.build();
    }

    @Override
    public List<CartItemResponseDTO> toItemResponseDTOs(List<CartItem> items) {
        if ( items == null ) {
            return null;
        }

        List<CartItemResponseDTO> list = new ArrayList<CartItemResponseDTO>( items.size() );
        for ( CartItem cartItem : items ) {
            list.add( toItemResponseDTO( cartItem ) );
        }

        return list;
    }

    private Long cartUserUserId(Cart cart) {
        if ( cart == null ) {
            return null;
        }
        User user = cart.getUser();
        if ( user == null ) {
            return null;
        }
        Long userId = user.getUserId();
        if ( userId == null ) {
            return null;
        }
        return userId;
    }

    private String cartItemProductProductName(CartItem cartItem) {
        if ( cartItem == null ) {
            return null;
        }
        Product product = cartItem.getProduct();
        if ( product == null ) {
            return null;
        }
        String productName = product.getProductName();
        if ( productName == null ) {
            return null;
        }
        return productName;
    }
}
