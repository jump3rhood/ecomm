package org.john.personal.productservice.service;

import org.john.personal.productservice.dto.CategoryResponseDTO;
import org.john.personal.productservice.dto.CategoryRequestDTO;


import java.util.List;

public interface CategoryService {
    List<CategoryResponseDTO> getAllCategories();
    CategoryResponseDTO getCategoryById(int id);
    CategoryResponseDTO createCategory(CategoryRequestDTO categoryRequestDTO);
    CategoryResponseDTO updateCategory(int id, CategoryRequestDTO categoryRequestDTO);
    void deleteCategory(int id);
}
