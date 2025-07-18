package com.john.ecommerce.productservice.service;

import com.john.ecommerce.productservice.dto.CategoryResponseDTO;
import com.john.ecommerce.productservice.dto.CategoryRequestDTO;
import com.john.ecommerce.productservice.exceptions.CategoryAlreadyExists;
import com.john.ecommerce.productservice.exceptions.CategoryNotFoundException;
import com.john.ecommerce.productservice.mapper.CategoryMapper;
import com.john.ecommerce.productservice.model.Category;
import com.john.ecommerce.productservice.repository.CategoryRepo;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepo categoryRepo;
    private final CategoryMapper categoryMapper;
    private final RedisTemplate<String, Object> redisTemplate;
    // constructor injection
    public CategoryServiceImpl(CategoryRepo categoryRepo, CategoryMapper categoryMapper, RedisTemplate<String, Object> redisTemplate) {
        this.categoryRepo = categoryRepo;
        this.categoryMapper = categoryMapper;
        this.redisTemplate = redisTemplate;
    }

    @Override
    @Cacheable(value="category-list", key="'all-categories'")
    public List<CategoryResponseDTO> getAllCategories() {
        List<Category> foundCategories = categoryRepo.findAll();
        List<CategoryResponseDTO> result = new ArrayList<>();
        for (Category category : foundCategories) {
            if(!category.getIsDeleted()){
                result.add(categoryMapper.categoryToCategoryResponseDTO(category));
            }
        }
        return result;
    }

    @Override
    public CategoryResponseDTO getCategoryById(int id) {
        CategoryResponseDTO responseDTO =(CategoryResponseDTO) redisTemplate.opsForHash().get("CATEGORIES", "CATEGORY_" + id );
        if(responseDTO != null){
            return responseDTO;
        }
        Optional<Category> optionalCategory = categoryRepo.findById(id);
        if(optionalCategory.isPresent()) {
            Category category = optionalCategory.get();
            if(!category.getIsDeleted()) {
                redisTemplate.opsForHash().put("CATEGORIES", "CATEGORY_"+ category.getId(), categoryMapper.categoryToCategoryResponseDTO(category));
                return categoryMapper.categoryToCategoryResponseDTO(category);
            }
        }
        throw new CategoryNotFoundException(id);
    }

    @Override
    @CacheEvict(value="category-list", allEntries = true)
    public CategoryResponseDTO createCategory(CategoryRequestDTO categoryRequestDTO) {
        Category oldCat = categoryRepo.getCategoryByTitle(categoryRequestDTO.getTitle().trim().toLowerCase());
        System.out.println(oldCat);
        if(oldCat != null) {
            throw new CategoryAlreadyExists("Category with title " + categoryRequestDTO.getTitle() + " exists.");
        }
        Category category = new Category();
        category.setTitle(categoryRequestDTO.getTitle().toLowerCase());
        category.setIsDeleted(false);
        Category savedCategory = categoryRepo.save(category);
        return categoryMapper.categoryToCategoryResponseDTO(savedCategory);
    }

    @Override
    @CacheEvict(value="category-list", allEntries = true)
    public CategoryResponseDTO updateCategory(int id, CategoryRequestDTO categoryRequestDTO) {
        Optional<Category> categoryOptional = categoryRepo.findById(id);
        if(categoryOptional.isEmpty()) {
            throw new CategoryNotFoundException(id);
        }
        Category category = categoryOptional.get();
        category.setTitle(categoryRequestDTO.getTitle());
        category.setIsDeleted(false);
        category = categoryRepo.save(category);
        redisTemplate.opsForHash().put("CATEGORIES", "CATEGORY_"+ category.getId(), category);
        return categoryMapper.categoryToCategoryResponseDTO(category);
    }

    @Override
    @CacheEvict(value="category-list", allEntries = true)
    public void deleteCategory(int id) {
        Optional<Category> categoryOptional = categoryRepo.findById(id);
        if(categoryOptional.isEmpty()) {
            return;
        }
        Category category = categoryOptional.get();
        category.setIsDeleted(true);
        redisTemplate.opsForHash().delete("CATEGORIES", "CATEGORY_"+ category.getId());
        categoryRepo.save(category);
    }
}
