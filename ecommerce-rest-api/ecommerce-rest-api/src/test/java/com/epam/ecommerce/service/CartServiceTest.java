package com.epam.ecommerce.service;

import com.epam.ecommerce.dto.CartResponseDTO;
import com.epam.ecommerce.exceptions.InvalidOperationException;
import com.epam.ecommerce.exceptions.ResourceNotFoundException;
import com.epam.ecommerce.mapper.CartMapper;
import com.epam.ecommerce.model.*;
import com.epam.ecommerce.repository.CartRepository;
import com.epam.ecommerce.repository.ProductRepository;
import com.epam.ecommerce.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CartServiceTest {

    @Mock
    private CartRepository cartRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private CartMapper cartMapper;

    @InjectMocks
    private CartService cartService;

    private User user;
    private Product product;
    private Cart cart;
    private CartResponseDTO responseDTO;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setUserId(1L);
        user.setUsername("John");
        user.setDeleted(false);

        product = new Product();
        product.setProductName("Laptop");
        product.setProductPrice(50000.0);
        product.setDeleted(false);

        cart = new Cart(user);

        responseDTO = new CartResponseDTO();
    }

    @Test
    void getCartByUserId_validId_shouldReturnCart() {
        when(userRepository.findByUserIdAndDeletedFalse(1L))
                .thenReturn(Optional.of(user));

        when(cartRepository.findCartByUser(user))
                .thenReturn(Optional.of(cart));

        Cart result = cartService.getCartByUserId(1L);

        assertNotNull(result);
    }

    @Test
    void getCartByUserId_cartCreatedIfNotExists_shouldCreateCart() {
        when(userRepository.findByUserIdAndDeletedFalse(1L))
                .thenReturn(Optional.of(user));

        when(cartRepository.findCartByUser(user))
                .thenReturn(Optional.empty());

        when(cartRepository.save(any(Cart.class)))
                .thenReturn(cart);

        Cart result = cartService.getCartByUserId(1L);

        assertNotNull(result);
        verify(cartRepository).save(any(Cart.class));
    }

    @Test
    void getCartByUsername_validUsername_shouldReturnCart() {
        when(userRepository.findByUsernameAndDeletedFalse("John"))
                .thenReturn(Optional.of(user));

        when(cartRepository.findCartByUser(user))
                .thenReturn(Optional.of(cart));

        Cart result = cartService.getCartByUsername("John");

        assertNotNull(result);
    }

    @Test
    void getCartByUsername_userNotFound_shouldThrowException() {
        when(userRepository.findByUsernameAndDeletedFalse("John"))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> cartService.getCartByUsername("John"));
    }

    @Test
    void getCartByUsername_cartNotFound_shouldThrowException() {
        when(userRepository.findByUsernameAndDeletedFalse("John"))
                .thenReturn(Optional.of(user));

        when(cartRepository.findCartByUser(user))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> cartService.getCartByUsername("John"));
    }

    @Test
    void addItemToCart_newItem_shouldReturnItemInCartDetails() {
        when(userRepository.findByUserIdAndDeletedFalse(1L)).thenReturn(Optional.of(user));
        when(productRepository.findByProductNameAndDeletedFalse("Laptop")).thenReturn(Optional.of(product));
        when(cartRepository.findCartByUser(user)).thenReturn(Optional.of(cart));
        when(cartMapper.toResponseDTO(cart)).thenReturn(responseDTO);

        CartResponseDTO result = cartService.addItemToCart(1L, "Laptop", 2);

        assertNotNull(result);
        verify(cartRepository).save(cart);
    }

    @Test
    void addItemToCart_invalidQuantity_shouldThrowException() {
        assertThrows(InvalidOperationException.class,
                () -> cartService.addItemToCart(1L, "Laptop", 0));
    }

    @Test
    void addItemToCart_quantityNull_shouldThrowException() {
        assertThrows(InvalidOperationException.class,
                () -> cartService.addItemToCart(1L, "Laptop", null));
    }

    @Test
    void addItemToCart_existingItem_increaseQuantity() {
        CartItem item = new CartItem(product, 2);
        cart.addItem(item);

        when(userRepository.findByUserIdAndDeletedFalse(1L))
                .thenReturn(Optional.of(user));

        when(productRepository.findByProductNameAndDeletedFalse("Laptop"))
                .thenReturn(Optional.of(product));

        when(cartRepository.findCartByUser(user))
                .thenReturn(Optional.of(cart));

        when(cartMapper.toResponseDTO(cart))
                .thenReturn(responseDTO);

        cartService.addItemToCart(1L, "Laptop", 3);

        assertEquals(5, item.getProductQuantity());
        verify(cartRepository).save(cart);
    }

    @Test
    void removeItemFromCart_reduceItemQuantity() {
        CartItem item = new CartItem(product, 2);
        cart.addItem(item);

        when(userRepository.findByUserIdAndDeletedFalse(1L)).thenReturn(Optional.of(user));
        when(cartRepository.findCartByUser(user)).thenReturn(Optional.of(cart));
        when(cartMapper.toResponseDTO(cart)).thenReturn(responseDTO);

        CartResponseDTO result = cartService.removeItemFromCart(1L, "Laptop");

        assertEquals(1, item.getProductQuantity());
        verify(cartRepository).save(cart);
        assertNotNull(result);
    }

    @Test
    void removeItemFromCart_removeItemCompletely() {
        CartItem item = new CartItem(product, 1);
        cart.addItem(item);

        when(userRepository.findByUserIdAndDeletedFalse(1L)).thenReturn(Optional.of(user));
        when(cartRepository.findCartByUser(user)).thenReturn(Optional.of(cart));
        when(cartMapper.toResponseDTO(cart)).thenReturn(responseDTO);

        cartService.removeItemFromCart(1L, "Laptop");

        assertTrue(cart.getCartItems().isEmpty());
        verify(cartRepository).save(cart);
    }

    @Test
    void removeItemFromCart_deletedProductInCart_shouldThrowException() {
        product.setDeleted(true);

        CartItem item = new CartItem(product, 2);
        cart.addItem(item);

        when(userRepository.findByUserIdAndDeletedFalse(1L))
                .thenReturn(Optional.of(user));

        when(cartRepository.findCartByUser(user))
                .thenReturn(Optional.of(cart));

        assertThrows(InvalidOperationException.class,
                () -> cartService.removeItemFromCart(1L, "Laptop"));
    }

    @Test
    void clearCart_existingCart_shouldRemoveAllItems() {
        CartItem item = new CartItem(product, 2);
        cart.addItem(item);

        when(userRepository.findByUserIdAndDeletedFalse(1L)).thenReturn(Optional.of(user));
        when(cartRepository.findCartByUser(user)).thenReturn(Optional.of(cart));
        when(cartMapper.toResponseDTO(cart)).thenReturn(responseDTO);

        CartResponseDTO result = cartService.clearCart(1L);

        assertTrue(cart.getCartItems().isEmpty());
        verify(cartRepository).save(cart);
        assertNotNull(result);
    }

    @Test
    void calculateTotalPrice_cartWithItems_shouldReturnCorrectTotal() {
        CartItem item = new CartItem(product, 2);
        cart.addItem(item);

        when(userRepository.findByUserIdAndDeletedFalse(1L)).thenReturn(Optional.of(user));
        when(cartRepository.findCartByUser(user)).thenReturn(Optional.of(cart));

        double total = cartService.calculateTotalPriceOfCart(1L);

        assertEquals(100000.0, total);
    }

    @Test
    void calculateTotalPrice_emptyCart_shouldThrowException() {
        when(userRepository.findByUserIdAndDeletedFalse(1L)).thenReturn(Optional.of(user));
        when(cartRepository.findCartByUser(user)).thenReturn(Optional.of(cart));

        assertThrows(InvalidOperationException.class,
                () -> cartService.calculateTotalPriceOfCart(1L));
    }

    @Test
    void calculateTotalPrice_allProductsDeleted_shouldThrowException() {

        product.setDeleted(true);

        CartItem item = new CartItem(product, 2);
        cart.addItem(item);

        when(userRepository.findByUserIdAndDeletedFalse(1L))
                .thenReturn(Optional.of(user));

        when(cartRepository.findCartByUser(user))
                .thenReturn(Optional.of(cart));

        assertThrows(InvalidOperationException.class,
                () -> cartService.calculateTotalPriceOfCart(1L));
    }

    @Test
    void getCartDTO_shouldMapToCartDTO() {
        when(userRepository.findByUserIdAndDeletedFalse(1L)).thenReturn(Optional.of(user));
        when(cartRepository.findCartByUser(user)).thenReturn(Optional.of(cart));
        when(cartMapper.toResponseDTO(cart)).thenReturn(responseDTO);

        CartResponseDTO result = cartService.getCartDTO(1L);

        assertNotNull(result);
    }

    @Test
    void getCart_userNotFound_shouldThrowException() {
        when(userRepository.findByUserIdAndDeletedFalse(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> cartService.getCartDTO(99L));
    }
}
