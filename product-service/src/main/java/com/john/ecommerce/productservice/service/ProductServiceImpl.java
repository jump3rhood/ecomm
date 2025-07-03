package com.john.ecommerce.productservice.service;

import com.john.ecommerce.productservice.dto.ProductRequestDTO;
import com.john.ecommerce.productservice.dto.ProductResponseDTO;
import com.john.ecommerce.productservice.exceptions.ProductNotFoundException;
import com.john.ecommerce.productservice.mapper.ProductMapper;
import com.john.ecommerce.productservice.model.Category;
import com.john.ecommerce.productservice.model.Product;
import com.john.ecommerce.productservice.repository.CategoryRepo;
import com.john.ecommerce.productservice.repository.ProductRepo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService {
    private final RedisTemplate<String, Object> redisTemplate;
    private final ProductRepo productRepo;
    private final CategoryRepo categoryRepo;
    private final ProductMapper productMapper;

    public ProductServiceImpl(ProductRepo productRepo, CategoryRepo categoryRepo, ProductMapper productMapper, RedisTemplate<String, Object> redisTemplate) {
        this.productRepo = productRepo;
        this.categoryRepo = categoryRepo;
        this.productMapper = productMapper;
        this.redisTemplate = redisTemplate;
    }

    @Override
    public List<ProductResponseDTO> getAllProducts() {
        // return only products with isDeleted = false
        List<Product> foundProducts = productRepo.findAll();
        List<ProductResponseDTO> result = new ArrayList<>();
        for(Product product: foundProducts){
            if(!product.getIsDeleted())
                result.add(productMapper.productToProductResponseDTO(product));
        }
        return result;
    }

    @Override
    public ProductResponseDTO getProductById(int id) {
        Optional<Product> optionalProduct = productRepo.findById(id);

        // first check if the product with id is present in redis
        ProductResponseDTO product = (ProductResponseDTO) redisTemplate.opsForHash().get("PRODUCTS", "PRODUCT_" + id);

        // cache HIT
        if(product != null){
            return product;
        }
        // cache MISS
        // return product with isDeleted = false
        if (optionalProduct.isEmpty()) {
            throw new ProductNotFoundException(id);
        }
        Product productFromDb = optionalProduct.get();
        ProductResponseDTO productResponseDTO = productMapper.productToProductResponseDTO(productFromDb);
        // Store in Redis
        redisTemplate.opsForHash().put("PRODUCTS", "PRODUCT_" + id, productResponseDTO);
        return productResponseDTO;
    }

    @Override
    public ProductResponseDTO createProduct(ProductRequestDTO productRequestDTO) {
        Product product = new Product();
        product.setTitle(productRequestDTO.getTitle());
        product.setDescription(productRequestDTO.getDescription());
        product.setPrice(productRequestDTO.getPrice());
        product.setIsDeleted(false);
        product.setImage(productRequestDTO.getImage());

        Category category = categoryRepo.getCategoryByTitle(productRequestDTO.getCategoryTitle());
        if(category == null){
            category = new Category();
            category.setTitle(productRequestDTO.getCategoryTitle());
            category.setIsDeleted(false);
            category = categoryRepo.save(category);
        }
        product.setCategory(category);
        Product savedProduct = productRepo.save(product);
        return productMapper.productToProductResponseDTO(savedProduct);
    }

    @Override
    public ProductResponseDTO updateProduct(int id, ProductRequestDTO productRequestDTO) {
        Optional<Product> optionalProduct = productRepo.findById(id);
        if(optionalProduct.isPresent()) {
            Product product = optionalProduct.get();
            product.setTitle(productRequestDTO.getTitle());
            product.setDescription(productRequestDTO.getDescription());
            product.setPrice(productRequestDTO.getPrice());
            product.setImage(productRequestDTO.getImage());
            product.setIsDeleted(false);
            product.setCategory(categoryRepo.getCategoryByTitle(productRequestDTO.getCategoryTitle()));
            Product updatedProduct = productRepo.save(product);
            return productMapper.productToProductResponseDTO(updatedProduct);
        }
        throw new ProductNotFoundException(id);
    }

    @Override
    public void deleteProduct(int id) {
        Optional<Product> optionalProduct = productRepo.findById(id);
        if(optionalProduct.isPresent()){
            Product product = optionalProduct.get();
            product.setIsDeleted(true);
            productRepo.save(product);
        }
    }

    @Override
    public Page<ProductResponseDTO> getAllProducts(Pageable pageable) {
        Page<Product> productPage = productRepo.findAll(pageable);
        return productPage.map(productMapper::productToProductResponseDTO);
    }

    @Override
    public List<ProductResponseDTO> getProductsByCategoryTitle(String categoryTitle) {
        List<Product> products = productRepo.findAllByCategoryTitle(categoryTitle);
        List<ProductResponseDTO> result = new ArrayList<>();
        for(Product product: products){
            result.add(productMapper.productToProductResponseDTO(product));
        }
        return result;
    }
}

