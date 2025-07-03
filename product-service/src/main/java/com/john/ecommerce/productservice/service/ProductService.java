package com.john.ecommerce.productservice.service;

import com.john.ecommerce.productservice.dto.ProductRequestDTO;
import com.john.ecommerce.productservice.dto.ProductResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProductService {
    List<ProductResponseDTO> getAllProducts();
    ProductResponseDTO getProductById(int id);
    ProductResponseDTO createProduct(ProductRequestDTO dto);
    ProductResponseDTO updateProduct(int id, ProductRequestDTO productRequestDTO);
    void deleteProduct(int id);
    Page<ProductResponseDTO> getAllProducts(Pageable pageable);
    List<ProductResponseDTO> getProductsByCategoryTitle(String categoryTitle);
}
