package org.john.personal.productservice.exceptions;


public class ProductNotFoundException extends RuntimeException{
    public ProductNotFoundException(Integer id) {
        super("Product with id " + id + " not found");
    }
}
