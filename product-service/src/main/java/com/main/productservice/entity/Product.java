package com.main.productservice.entity;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "products")
public class Product {

    @Id
    private String productId;

    @NotBlank
    @Size(min = 3, max = 50, message = "Product name must contain at least 3 characters")
    private String productName;

    private String image;

    @NotBlank
    @Size(min = 6, message = "Description must contain at least 6 characters")
    private String description;

    private Integer quantity;
    private Double price;
    private Double discount;
    private Double specialPrice;

    private String categoryId;
    private String sellerId;
}

