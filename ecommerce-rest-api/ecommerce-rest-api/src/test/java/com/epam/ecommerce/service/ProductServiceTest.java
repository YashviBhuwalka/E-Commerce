package com.epam.ecommerce.service;

import com.epam.ecommerce.dto.ProductRequestDTO;
import com.epam.ecommerce.dto.ProductResponseDTO;
import com.epam.ecommerce.exceptions.InvalidOperationException;
import com.epam.ecommerce.exceptions.ResourceNotFoundException;
import com.epam.ecommerce.mapper.ProductMapper;
import com.epam.ecommerce.model.Product;
import com.epam.ecommerce.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository repository;

    @Mock
    private ProductMapper productMapper;

    @InjectMocks
    private ProductService productService;

    private Product product;
    private ProductRequestDTO requestDTO;
    private ProductResponseDTO responseDTO;

    @BeforeEach
    void setUp() {
        product = new Product();
        product.setProductId(1);
        product.setProductName("Laptop");
        product.setProductCategory("Electronics");
        product.setProductPrice(50000.0);
        product.setDeleted(false);

        requestDTO = new ProductRequestDTO();
        requestDTO.setProductName("Laptop");
        requestDTO.setProductCategory("Electronics");
        requestDTO.setProductPrice(50000.0);

        responseDTO = new ProductResponseDTO();
        responseDTO.setProductName("Laptop");
        responseDTO.setProductCategory("Electronics");
        responseDTO.setProductPrice(50000.0);
    }

    @Test
    void addProduct_validRequest_shouldReturnProductDetails() {
        when(productMapper.toEntity(requestDTO)).thenReturn(product);
        when(repository.save(product)).thenReturn(product);
        when(productMapper.toDTO(product)).thenReturn(responseDTO);

        ProductResponseDTO result = productService.addProduct(requestDTO);

        assertNotNull(result);
        assertEquals("Laptop", result.getProductName());
        verify(repository, times(1)).save(product);
    }

    @Test
    void updateProduct_shouldReturnUpdatedProductDetails() {
        when(repository.findById(1)).thenReturn(Optional.of(product));
        when(repository.save(product)).thenReturn(product);
        when(productMapper.toDTO(product)).thenReturn(responseDTO);

        ProductResponseDTO result = productService.updateProduct(1, requestDTO);

        assertEquals("Laptop", product.getProductName());
        verify(repository, times(1)).save(product);
    }

    @Test
    void updateProduct_notFound_shouldThrowException() {
        when(repository.findById(2)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> productService.updateProduct(2, requestDTO));
    }

    @Test
    void deleteProduct_shouldMarkProductAsDeleted() {
        when(repository.findById(1)).thenReturn(Optional.of(product));

        productService.deleteProduct(1);

        assertTrue(product.isDeleted());
        verify(repository, times(1)).save(product);
    }

    @Test
    void deleteProduct_alreadyDeleted_shouldThrowException() {
        product.setDeleted(true);
        when(repository.findById(1)).thenReturn(Optional.of(product));

        assertThrows(InvalidOperationException.class,
                () -> productService.deleteProduct(1));
    }

    @Test
    void deleteProduct_notFound_shouldThrowException() {
        when(repository.findById(2)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> productService.deleteProduct(2));
    }

    @Test
    void getProductById_validId_shouldReturnProduct() {
        when(repository.findById(1)).thenReturn(Optional.of(product));
        when(productMapper.toDTO(product)).thenReturn(responseDTO);

        ProductResponseDTO result = productService.getProductById(1);

        assertNotNull(result);
        assertEquals("Laptop", result.getProductName());
    }

    @Test
    void getProductById_notFound_shouldThrowException() {
        when(repository.findById(2)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> productService.getProductById(2));
    }

    @Test
    void getAllProducts_shouldReturnAllProducts() {
        when(repository.findAll()).thenReturn(List.of(product));
        when(productMapper.toDTO(product)).thenReturn(responseDTO);

        List<ProductResponseDTO> result = productService.getAllProducts();

        assertEquals(1, result.size());
        assertEquals("Laptop", result.get(0).getProductName());
    }

    @Test
    void getAllActiveProducts_shouldReturnActiveProducts() {
        when(repository.findByDeletedFalse()).thenReturn(List.of(product));
        when(productMapper.toDTO(product)).thenReturn(responseDTO);

        List<ProductResponseDTO> result = productService.getAllActiveProducts();

        assertEquals(1, result.size());
    }

    @Test
    void searchProductsByName_shouldReturnMatchingProducts() {
        when(repository.findByProductNameContainingIgnoreCaseAndDeletedFalse("Lap"))
                .thenReturn(List.of(product));
        when(productMapper.toDTO(product)).thenReturn(responseDTO);

        List<ProductResponseDTO> result =
                productService.searchProductsByName("Lap");

        assertEquals(1, result.size());
    }

    @Test
    void searchProductsByCategory_shouldReturnMatchingProducts() {
        when(repository.findByProductCategoryContainingIgnoreCaseAndDeletedFalse("Electronics"))
                .thenReturn(List.of(product));
        when(productMapper.toDTO(product)).thenReturn(responseDTO);

        List<ProductResponseDTO> result =
                productService.searchProductsByCategory("Electronics");

        assertEquals(1, result.size());
    }

    @Test
    void searchProductsByPriceRange_shouldReturnMatchingProducts() {
        when(repository.findByProductPriceBetweenAndDeletedFalse(40000.0, 60000.0))
                .thenReturn(List.of(product));
        when(productMapper.toDTO(product)).thenReturn(responseDTO);

        List<ProductResponseDTO> result =
                productService.searchProductsByPriceRange(40000.0, 60000.0);

        assertEquals(1, result.size());
    }
}
