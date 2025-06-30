package com.main.productservice.payloads.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ProductUpdateRequest {
    @NotBlank
    @Size(min = 3, max = 50, message = "Product id must not be null")
    private String productId;

    private String productName;

    private String image;

    @Size(min = 6, message = "Description must contain at least 6 characters")
    private String description;

    private Integer quantity;
    private Double price;
    private Double discount;

    private String categoryId;
    private String sellerId;
}
