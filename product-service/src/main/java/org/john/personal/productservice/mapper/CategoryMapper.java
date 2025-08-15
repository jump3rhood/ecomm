package org.john.personal.productservice.mapper;

import org.john.personal.productservice.dto.CategoryResponseDTO;
import org.john.personal.productservice.model.Category;
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
