package com.john.ecommerce.productservice.controller;

import com.john.ecommerce.productservice.dto.CategoryResponseDTO;
import com.john.ecommerce.productservice.dto.CategoryRequestDTO;
import com.john.ecommerce.productservice.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    private final CategoryService categoryService;

    // constructor injection
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }


    @GetMapping("/")
    public ResponseEntity<List<CategoryResponseDTO>> getAllCategories() {
        List<CategoryResponseDTO> categories = categoryService.getAllCategories();
        return ResponseEntity.ok(categories);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryResponseDTO> getCategoryById(@PathVariable int id) {

        CategoryResponseDTO dto = categoryService.getCategoryById(id);
        return ResponseEntity.ok(dto);
    }
    @PostMapping("/")
    public ResponseEntity<CategoryResponseDTO> createCategory(@Valid @RequestBody CategoryRequestDTO categoryRequestDTO) {
        CategoryResponseDTO dto = categoryService.createCategory(categoryRequestDTO);
        return new ResponseEntity<>(dto, HttpStatus.CREATED);
    }
    @PutMapping("/{id}")
    public ResponseEntity<CategoryResponseDTO> updateCategory(@PathVariable int id, @Valid @RequestBody CategoryRequestDTO categoryRequestDTO) {
        CategoryResponseDTO category = categoryService.updateCategory(id, categoryRequestDTO);
        return new ResponseEntity<>(category, HttpStatus.OK);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCategory(@PathVariable int id) {
        categoryService.deleteCategory(id);
        return new ResponseEntity<>("Category deleted with id " + id, HttpStatus.OK);
    }

}
