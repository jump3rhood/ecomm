package com.john.ecommerce.productservice.repository;

import com.john.ecommerce.productservice.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ProductRepo extends JpaRepository<Product, Integer> {

    Product findProductById(int id);
    List<Product> findAll();
    Product save(Product product);
    // pagination and sorting
    Page<Product> findAll(Pageable pageable);

    List<Product> findAllByCategoryTitle(String categoryTitle);

    Optional<Product> findByIdAndIsDeletedFalse(int id);
}
