package com.main.productservice.payloads.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ProductRequest {
    @NotBlank
    @Size(min = 3, max = 50, message = "Product name must contain at least 3 characters")
    private String productName;

    @NotBlank
    @Size(min = 6, message = "Description must contain at least 6 characters")
    private String description;

    private Integer quantity;
    private Double price;
    private Double discount;

    @NotBlank(message = "Category id must not be blanked")
    private String categoryId;
    private String sellerId;
}
