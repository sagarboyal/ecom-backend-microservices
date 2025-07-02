package com.main.orderservice.payloads.product;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProductResponse {
    private String productId;
    private String productName;
    private String image;
    private String description;
    private Integer quantity;
    private Double price;
    private Double discount;
    private Double specialPrice;
    private String categoryName;
    private String sellerName;
}
