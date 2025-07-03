package com.john.ecommerce.productservice.exceptions;

public class CategoryNotFoundException extends RuntimeException {
    public CategoryNotFoundException(Integer id) {
        super("Category with id " + id + " not found");
    }
}
