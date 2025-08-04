package com.main.orderservice.payloads.cart;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CartRequest {
    @NotBlank
    private String productId;
    @NotBlank
    private Long userId;
    private Integer quantity;
    private String operation;
}
