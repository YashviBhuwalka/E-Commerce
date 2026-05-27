package com.epam.ecommerce.mapper;

import com.epam.ecommerce.dto.ProductRequestDTO;
import com.epam.ecommerce.dto.ProductResponseDTO;
import com.epam.ecommerce.model.Product;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-05-17T23:38:58+0530",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 21.0.9 (Oracle Corporation)"
)
@Component
public class ProductMapperImpl implements ProductMapper {

    @Override
    public Product toEntity(ProductRequestDTO dto) {
        if ( dto == null ) {
            return null;
        }

        Product product = new Product();

        product.setProductName( dto.getProductName() );
        product.setProductCategory( dto.getProductCategory() );
        product.setProductPrice( dto.getProductPrice() );

        return product;
    }

    @Override
    public ProductResponseDTO toDTO(Product product) {
        if ( product == null ) {
            return null;
        }

        ProductResponseDTO productResponseDTO = new ProductResponseDTO();

        productResponseDTO.setProductId( product.getProductId() );
        productResponseDTO.setProductName( product.getProductName() );
        productResponseDTO.setProductCategory( product.getProductCategory() );
        productResponseDTO.setProductPrice( product.getProductPrice() );
        productResponseDTO.setDeleted( product.isDeleted() );

        return productResponseDTO;
    }
}
