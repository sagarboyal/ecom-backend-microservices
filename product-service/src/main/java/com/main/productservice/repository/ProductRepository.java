package com.main.productservice.repository;

import com.main.productservice.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ProductRepository extends MongoRepository<Product, String>{
    Page<Product> findAllByCategoryId(String categoryId, Pageable pageDetails);
    boolean existsByProductNameEqualsIgnoreCase(String productName);
    Page<Product> findByProductNameLikeIgnoreCase(String productName, Pageable pageable);
}
