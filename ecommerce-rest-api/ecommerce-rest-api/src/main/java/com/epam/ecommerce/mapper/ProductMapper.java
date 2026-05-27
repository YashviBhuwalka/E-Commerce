package com.epam.ecommerce.mapper;

import com.epam.ecommerce.dto.ProductRequestDTO;
import com.epam.ecommerce.dto.ProductResponseDTO;
import com.epam.ecommerce.model.Product;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    Product toEntity(ProductRequestDTO dto);

    ProductResponseDTO toDTO(Product product);
}
