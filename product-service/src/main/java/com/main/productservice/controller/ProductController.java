package com.main.productservice.controller;

import com.main.productservice.constrains.AppConstraints;
import com.main.productservice.payloads.request.ProductRequest;
import com.main.productservice.payloads.request.ProductUpdateRequest;
import com.main.productservice.payloads.response.PageResponse;
import com.main.productservice.payloads.response.ProductResponse;
import com.main.productservice.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {
   private final ProductService productService;

    @GetMapping
    public ResponseEntity<PageResponse<ProductResponse>> getAllProducts(
            @RequestParam(name = "pageNumber", defaultValue = AppConstraints.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(name = "pageSize", defaultValue = AppConstraints.PAGE_SIZE, required = false) Integer pageSize,
            @RequestParam(name = "sortBy", defaultValue = AppConstraints.DEFAULT_SORT_BY_PRODUCT, required = false) String sortBy,
            @RequestParam(name = "sortOrder", defaultValue = AppConstraints.SORT_DIR, required = false) String sortOrder
    ) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(productService.getAllProducts(pageNumber, pageSize, sortBy, sortOrder));
    }

    @GetMapping("/{productId}")
    public ResponseEntity<ProductResponse> getProductById(@PathVariable String productId) {
                  return ResponseEntity
                .status(HttpStatus.OK)
                .body(productService
                        .getProduct(productId));
    }


    @GetMapping("/categories/{categoryId}/products")
    public ResponseEntity<PageResponse<ProductResponse>> getAllProductsByCategory(
            @PathVariable String categoryId,
            @RequestParam(name = "pageNumber", defaultValue = AppConstraints.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(name = "pageSize", defaultValue = AppConstraints.PAGE_SIZE, required = false) Integer pageSize,
            @RequestParam(name = "sortBy", defaultValue = AppConstraints.DEFAULT_SORT_BY_PRODUCT, required = false) String sortBy,
            @RequestParam(name = "sortOrder", defaultValue = AppConstraints.SORT_DIR, required = false) String sortOrder
    ) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(productService
                        .getAllProductsByCategoryId(categoryId, pageNumber, pageSize, sortBy, sortOrder));
    }

    @GetMapping("/keyword/{keyword}")
    public ResponseEntity<PageResponse<ProductResponse>> getAllProductsByKeyWord(
            @PathVariable String keyword,
            @RequestParam(name = "pageNumber", defaultValue = AppConstraints.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(name = "pageSize", defaultValue = AppConstraints.PAGE_SIZE, required = false) Integer pageSize,
            @RequestParam(name = "sortBy", defaultValue = AppConstraints.DEFAULT_SORT_BY_PRODUCT, required = false) String sortBy,
            @RequestParam(name = "sortOrder", defaultValue = AppConstraints.SORT_DIR, required = false) String sortOrder) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(productService
                        .getAllProductsByCategoryKeyword(keyword,pageNumber, pageSize, sortBy, sortOrder ));
    }

    @PostMapping
    public ResponseEntity<ProductResponse> addProduct(@Valid @RequestBody ProductRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(productService.addProduct(request));
    }
    @PutMapping
    public ResponseEntity<ProductResponse> updateProduct(@RequestBody ProductUpdateRequest request) {
        return ResponseEntity.status(HttpStatus.OK).body(productService.updateProduct(request));
    }
    @PutMapping("/{productId}/image")
    public ResponseEntity<ProductResponse> updateProductImage(@Valid @PathVariable String productId,
                                                         @RequestParam("image") MultipartFile image) throws IOException {
        return ResponseEntity.status(HttpStatus.OK).body(productService.updateProductImage(productId, image));
    }
    @DeleteMapping("/{productId}")
    public ResponseEntity<ProductResponse> deleteProduct(@PathVariable String productId) {
        return ResponseEntity.status(HttpStatus.OK).body(productService.deleteProduct(productId));
    }
}
