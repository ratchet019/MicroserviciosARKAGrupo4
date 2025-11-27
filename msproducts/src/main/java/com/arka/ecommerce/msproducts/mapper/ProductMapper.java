package com.arka.ecommerce.msproducts.mapper;

import com.arka.ecommerce.msproducts.dto.ProductDtoRequest;
import com.arka.ecommerce.msproducts.dto.ProductDtoResponse;
import com.arka.ecommerce.msproducts.model.Product;

public class ProductMapper {

    public static ProductDtoResponse toDto(Product product) {
        return new ProductDtoResponse(
            product.getName(),
            product.getDescription(),
            product.getPrice(),
            product.getCategoryId(),
            product.getCreatedAt()
        );
    }

    public static Product toEntity(ProductDtoRequest dto) {
        return new Product(
            dto.getId(),
            dto.getName(),
            dto.getDescription(),
            dto.getPrice(),
            dto.getCategoryId(),
            dto.getReorderPoint(),
            dto.getStatus(),
            dto.getCreatedAt()
        );
    }
}
