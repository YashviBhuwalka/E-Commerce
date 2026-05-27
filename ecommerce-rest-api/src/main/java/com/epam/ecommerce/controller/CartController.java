package com.epam.ecommerce.controller;

import com.epam.ecommerce.dto.CartItemRequestDTO;
import com.epam.ecommerce.dto.CartRequestDTO;
import com.epam.ecommerce.dto.CartResponseDTO;
import com.epam.ecommerce.service.CartService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/carts")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    /**
     * Add a product to a user's cart
     */
    @PostMapping("/{userId}/items")
    public ResponseEntity<CartResponseDTO> addItemToCart(
            @PathVariable Long userId,
            @Valid @RequestBody CartRequestDTO cartRequestDTO) {

        for (CartItemRequestDTO item : cartRequestDTO.getItems()) {
            cartService.addItemToCart(
                    userId,
                    item.getProductName(),
                    item.getQuantity()
            );
        }

        CartResponseDTO updatedCart = cartService.getCartDTO(userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(updatedCart);
    }

    /**
     * Remove a product from the user's cart
     */
    @DeleteMapping("/{userId}/items/{productName}")
    public ResponseEntity<CartResponseDTO> removeItemFromCart(
            @PathVariable Long userId,
            @PathVariable String productName) {

        cartService.removeItemFromCart(userId, productName);
        CartResponseDTO updatedCart = cartService.getCartDTO(userId);
        return ResponseEntity.ok(updatedCart);
    }

    /**
     * View cart contents for a user
     */
    @GetMapping("/{userId}")
    public ResponseEntity<CartResponseDTO> viewCart(@PathVariable Long userId) {
        CartResponseDTO cartDTO = cartService.getCartDTO(userId);
        return ResponseEntity.ok(cartDTO);
    }

    /**
     * Clear cart for a user
     */
    @DeleteMapping("/{userId}/clear")
    public ResponseEntity<Void> clearCart(@PathVariable Long userId) {
        cartService.clearCart(userId);
        return ResponseEntity.noContent().build();
    }
}