package com.main.productservice.service;

import com.main.productservice.payloads.request.ProductRequest;
import com.main.productservice.payloads.request.ProductUpdateRequest;
import com.main.productservice.payloads.response.PageResponse;
import com.main.productservice.payloads.response.ProductResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ProductService {
    ProductResponse addProduct(ProductRequest productRequest);
    PageResponse<ProductResponse> getAllProducts(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);
    PageResponse<ProductResponse> getAllProductsByCategoryId(String categoryId, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);
    PageResponse<ProductResponse> getAllProductsByCategoryKeyword(String keyword, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);
    ProductResponse updateProduct(ProductUpdateRequest productRequest);
    ProductResponse updateProductImage(String productId, MultipartFile image) throws IOException;
    ProductResponse deleteProduct(String productId);
    ProductResponse getProduct(String productId);
}
