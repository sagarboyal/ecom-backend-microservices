package com.main.orderservice.client.productservice;

import com.main.orderservice.payloads.product.ProductResponse;
import com.main.orderservice.payloads.product.ProductUpdateRequest;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PutExchange;

@HttpExchange
public interface ProductHttpInterfaceProvider {
    @GetExchange("/api/products/{productId}")
    ProductResponse getProductById(@PathVariable("productId") String productId);
    @PutExchange("/api/products")
    void updateProduct(@RequestBody ProductUpdateRequest request);
}
