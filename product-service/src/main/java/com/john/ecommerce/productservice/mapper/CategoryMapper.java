package com.john.ecommerce.productservice.mapper;

import com.john.ecommerce.productservice.dto.CategoryResponseDTO;
import com.john.ecommerce.productservice.model.Category;
import org.springframework.stereotype.Component;

@Component
public class CategoryMapper {

    public CategoryResponseDTO categoryToCategoryResponseDTO(Category category) {
        CategoryResponseDTO dto = new CategoryResponseDTO();
        dto.setId(category.getId());
        dto.setTitle(category.getTitle());
        return dto;
    }
}
