package com.john.ecommerce.productservice.service;

import com.john.ecommerce.productservice.dto.CategoryResponseDTO;
import com.john.ecommerce.productservice.dto.CategoryRequestDTO;


import java.util.List;

public interface CategoryService {
    List<CategoryResponseDTO> getAllCategories();
    CategoryResponseDTO getCategoryById(int id);
    CategoryResponseDTO createCategory(CategoryRequestDTO categoryRequestDTO);
    CategoryResponseDTO updateCategory(int id, CategoryRequestDTO categoryRequestDTO);
    void deleteCategory(int id);
}
