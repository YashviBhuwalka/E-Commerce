package com.epam.ecommerce.service;

import com.epam.ecommerce.dto.*;
import com.epam.ecommerce.exceptions.InvalidOperationException;
import com.epam.ecommerce.exceptions.ResourceNotFoundException;
import com.epam.ecommerce.mapper.CartMapper;
import com.epam.ecommerce.model.Cart;
import com.epam.ecommerce.model.CartItem;
import com.epam.ecommerce.model.Product;
import com.epam.ecommerce.model.User;
import com.epam.ecommerce.repository.CartRepository;
import com.epam.ecommerce.repository.ProductRepository;
import com.epam.ecommerce.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Transactional
public class CartService {

    private final CartRepository cartRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final CartMapper cartMapper;

    public CartService(CartRepository cartRepository,
                       UserRepository userRepository,
                       ProductRepository productRepository,
                       CartMapper cartMapper) {
        this.cartRepository = cartRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
        this.cartMapper = cartMapper;
    }

    private User getUserById(Long userId) {
        return userRepository.findByUserIdAndDeletedFalse(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    private Cart getOrCreateCart(User user) {
        return cartRepository.findCartByUser(user)
                .orElseGet(() -> cartRepository.save(new Cart(user)));
    }

    private Cart getCartByUser(User user) {
        return cartRepository.findCartByUser(user)
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found"));
    }

    public Cart getCartByUserId(Long userId) {
        User user = getUserById(userId);
        return getOrCreateCart(user);
    }

    public Cart getCartByUsername(String username) {
        User user = userRepository.findByUsernameAndDeletedFalse(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        return cartRepository.findCartByUser(user)
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found"));
    }

    public CartResponseDTO addItemToCart(Long userId, String productName, Integer quantity) {
        if (quantity == null || quantity <= 0) {
            throw new InvalidOperationException("Quantity must be greater than zero");
        }

        User user = getUserById(userId);
        Product product = productRepository.findByProductNameAndDeletedFalse(productName)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        Cart cart = getOrCreateCart(user);

        Optional<CartItem> existingItemOpt = cart.getCartItems().stream()
                .filter(item -> item.getProduct().getProductName().equalsIgnoreCase(productName))
                .findFirst();

        if (existingItemOpt.isPresent()) {
            CartItem existingItem = existingItemOpt.get();
            existingItem.increaseQuantity(quantity);
        } else {
            CartItem newItem = new CartItem(product, quantity);
            cart.addItem(newItem);
        }

        cartRepository.save(cart);

        return cartMapper.toResponseDTO(cart);
    }

    public CartResponseDTO removeItemFromCart(Long userId, String productName) {
        User user = getUserById(userId);
        Cart cart = getCartByUser(user);

        CartItem cartItem = cart.getCartItems().stream()
                .filter(item -> item.getProduct().getProductName().equalsIgnoreCase(productName) && !item.getProduct().isDeleted())
                .findFirst()
                .orElseThrow(() -> new InvalidOperationException("Product not found in cart"));

        if (cartItem.getProductQuantity() > 1) {
            cartItem.setProductQuantity(cartItem.getProductQuantity() - 1);
        } else {
            cart.getCartItems().remove(cartItem);
        }

        cartRepository.save(cart);

        return cartMapper.toResponseDTO(cart);
    }

    public CartResponseDTO clearCart(Long userId) {
        User user = getUserById(userId);
        Cart cart = getCartByUser(user);

        cart.getCartItems().clear();
        cartRepository.save(cart);

        return cartMapper.toResponseDTO(cart);
    }

    public double calculateTotalPriceOfCart(Long userId) {

        User user = userRepository.findByUserIdAndDeletedFalse(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Cart cart = getCartByUser(user);

        if (cart.getCartItems().isEmpty()) {
            throw new InvalidOperationException("Cart is empty");
        }

        double total = cart.getCartItems().stream()
                .filter(item -> !item.getProduct().isDeleted())
                .mapToDouble(CartItem::getTotalPrice)
                .sum();

        if (total == 0) {
            throw new InvalidOperationException("All products in cart are unavailable");
        }

        return total;
    }

    public CartResponseDTO getCartDTO(Long userId) {
        User user = getUserById(userId);
        Cart cart = getOrCreateCart(user);
        return cartMapper.toResponseDTO(cart);
    }
}