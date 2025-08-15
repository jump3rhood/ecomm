package org.john.personal.productservice.mapper;

import org.john.personal.productservice.dto.ProductResponseDTO;
import org.john.personal.productservice.model.Product;
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
