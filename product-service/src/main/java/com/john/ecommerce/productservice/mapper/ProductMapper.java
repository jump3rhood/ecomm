package com.john.ecommerce.productservice.mapper;

import com.john.ecommerce.productservice.dto.ProductResponseDTO;
import com.john.ecommerce.productservice.model.Product;
import org.springframework.stereotype.Component;

@Component
public class ProductMapper {
    public ProductResponseDTO productToProductResponseDTO(Product product) {
        ProductResponseDTO dto = new ProductResponseDTO();
        dto.setId(product.getId());
        dto.setTitle(product.getTitle());
        dto.setDescription(product.getDescription());
        dto.setPrice(product.getPrice().toString());
        dto.setImage(product.getImage());
        if(product.getCategory() != null)
            dto.setCategoryTitle(product.getCategory().getTitle());
        return dto;
    }
}
