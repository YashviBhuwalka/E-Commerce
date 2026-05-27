package com.epam.ecommerce.controller;

import com.epam.ecommerce.dto.ProductResponseDTO;
import com.epam.ecommerce.service.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class UserProductController {

    private final ProductService productService;

    public UserProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public ResponseEntity<List<ProductResponseDTO>> getAllProducts() {
        List<ProductResponseDTO> products = productService.getAllActiveProducts();
        return ResponseEntity.ok(products);
    }

    @GetMapping("/search/name")
    public ResponseEntity<List<ProductResponseDTO>> searchProductByName(
            @RequestParam String name) {
        List<ProductResponseDTO> products = productService.searchProductsByName(name);
        return ResponseEntity.ok(products);
    }

    @GetMapping("/search/category")
    public ResponseEntity<List<ProductResponseDTO>> searchProductByCategory(
            @RequestParam String category) {
        List<ProductResponseDTO> products = productService.searchProductsByCategory(category);
        return ResponseEntity.ok(products);
    }

    @GetMapping("/search/price")
    public ResponseEntity<List<ProductResponseDTO>> searchProductByPriceRange(
            @RequestParam Double minPrice,
            @RequestParam Double maxPrice) {
        List<ProductResponseDTO> products = productService.searchProductsByPriceRange(minPrice, maxPrice);
        return ResponseEntity.ok(products);
    }
}
