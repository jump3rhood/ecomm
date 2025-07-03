package com.john.ecommerce.productservice.controller;

import com.john.ecommerce.productservice.dto.ProductRequestDTO;
import com.john.ecommerce.productservice.dto.ProductResponseDTO;
import com.john.ecommerce.productservice.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/*
* 1. GET ALL PRODUCTS
* 2. GET PRODUCT BY ID
* 3. POST PRODUCTS
* 4. PUT PRODUCT BY ID
* 5. DELETE PRODUCT
* 6. GET ALL PRODUCTS?CATEGORY=TITLE
* */

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/")
    public ResponseEntity<List<ProductResponseDTO>> getProducts() {
        return new ResponseEntity<>(productService.getAllProducts(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponseDTO> getProduct(@PathVariable Integer id)
    {
        ProductResponseDTO productResponseDTO = productService.getProductById(id);
        return ResponseEntity.ok(productResponseDTO);
    }

    @PostMapping("/")
    public ResponseEntity<ProductResponseDTO> createProduct(@Valid @RequestBody ProductRequestDTO productRequestDTO) {
        return new ResponseEntity<>(productService.createProduct(productRequestDTO), HttpStatus.CREATED);
    }
    @PutMapping("/{id}")
    public ResponseEntity<ProductResponseDTO> updateProduct(@PathVariable Integer id, @RequestBody ProductRequestDTO productRequestDTO)  {
        return new ResponseEntity<>(productService.updateProduct(id, productRequestDTO), HttpStatus.OK);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable Integer id){
        productService.deleteProduct(id);
        return new ResponseEntity<>("Product deleted", HttpStatus.OK);
    }

    @GetMapping("/list")
    public ResponseEntity<Page<ProductResponseDTO>> getProductsPageable(
            @RequestParam(defaultValue = "0") int pageNumber,
            @RequestParam(defaultValue = "3") int pageSize,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "true") boolean ascending
    ) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize, ascending ? Sort.by(sortBy).ascending(): Sort.by(sortBy).descending());
        Page<ProductResponseDTO> productPage = productService.getAllProducts(pageable);
        return new ResponseEntity<>(productPage, HttpStatus.OK);
    }

    // Get products in a category
    @GetMapping("/category/{categoryTitle}")
    public ResponseEntity<List<ProductResponseDTO>> getProductsByCategoryName(@PathVariable String categoryTitle) {
        return ResponseEntity.ok(productService.getProductsByCategoryTitle(categoryTitle));
    }

}
