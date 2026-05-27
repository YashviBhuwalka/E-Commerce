package com.epam.ecommerce.repository;

import com.epam.ecommerce.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Integer> {

    List<Order> findOrderByUser_UserIdAndUser_DeletedFalse(Long userId);

    List<Order> findOrderByUser_UserId(Long userId);

}
