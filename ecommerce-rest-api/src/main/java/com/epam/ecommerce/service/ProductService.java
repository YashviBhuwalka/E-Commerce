package com.epam.ecommerce.service;

import com.epam.ecommerce.dto.ProductRequestDTO;
import com.epam.ecommerce.dto.ProductResponseDTO;
import com.epam.ecommerce.exceptions.InvalidOperationException;
import com.epam.ecommerce.exceptions.ResourceNotFoundException;
import com.epam.ecommerce.model.Product;
import com.epam.ecommerce.repository.ProductRepository;
import com.epam.ecommerce.mapper.ProductMapper;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class ProductService {

    private final ProductRepository repository;
    private final ProductMapper productMapper;

    public ProductService(ProductRepository repository, ProductMapper productMapper) {
        this.repository = repository;
        this.productMapper = productMapper;
    }

    public ProductResponseDTO addProduct(ProductRequestDTO dto) {
        Product product = productMapper.toEntity(dto);
        Product savedProduct = repository.save(product);
        return productMapper.toDTO(savedProduct);
    }

    public ProductResponseDTO updateProduct(Integer id, ProductRequestDTO dto) {
        Product product = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));

        product.setProductName(dto.getProductName());
        product.setProductCategory(dto.getProductCategory());
        product.setProductPrice(dto.getProductPrice());

        Product updatedProduct = repository.save(product);
        return productMapper.toDTO(updatedProduct);
    }

    public void deleteProduct(Integer id) {
        Product product = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));
        if (product.isDeleted()) {
            throw new InvalidOperationException("Product already deleted");
        }

        product.setDeleted(true);
        repository.save(product);
    }

    public ProductResponseDTO getProductById(Integer id) {
        Product product = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));
        return productMapper.toDTO(product);
    }

    public List<ProductResponseDTO> getAllProducts() {
        List<Product> products = repository.findAll();
        return products.stream()
                .map(productMapper::toDTO)
                .toList();
    }

    public List<ProductResponseDTO> getAllActiveProducts() {
        return repository.findByDeletedFalse()
                .stream()
                .map(productMapper::toDTO)
                .toList();
    }

    public List<ProductResponseDTO> searchProductsByName(String name) {
        return repository.findByProductNameContainingIgnoreCaseAndDeletedFalse(name)
                .stream()
                .map(productMapper::toDTO)
                .toList();
    }

    public List<ProductResponseDTO> searchProductsByCategory(String category) {
        return repository.findByProductCategoryContainingIgnoreCaseAndDeletedFalse(category)
                .stream()
                .map(productMapper::toDTO)
                .toList();
    }

    public List<ProductResponseDTO> searchProductsByPriceRange(Double minPrice, Double maxPrice) {
        return repository.findByProductPriceBetweenAndDeletedFalse(minPrice, maxPrice)
                .stream()
                .map(productMapper::toDTO)
                .toList();
    }
}