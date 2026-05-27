package com.epam.ecommerce.repository;

import com.epam.ecommerce.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Integer> {

    Optional<Product> findByProductIdAndDeletedFalse(Integer productId);

    List<Product> findByDeletedFalse();

    Optional<Product> findByProductNameAndDeletedFalse(String productName);

    List<Product> findByProductNameContainingIgnoreCaseAndDeletedFalse(String name);

    List<Product> findByProductCategoryContainingIgnoreCaseAndDeletedFalse(String category);

    List<Product> findByProductPriceBetweenAndDeletedFalse(Double minPrice, Double maxPrice);

}
