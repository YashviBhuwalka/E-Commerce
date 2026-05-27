package com.epam.ecommerce.repository;

import com.epam.ecommerce.model.Cart;
import com.epam.ecommerce.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Integer> {

    Optional<Cart> findCartByUser(User user);
}