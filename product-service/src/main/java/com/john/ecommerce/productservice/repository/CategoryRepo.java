package com.john.ecommerce.productservice.repository;

import com.john.ecommerce.productservice.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryRepo extends JpaRepository<Category, Integer> {
    List<Category> findAll();
    Category findCategoryById(int id);
    Category save(Category category);
    Category getCategoryByTitle(String title);
}
