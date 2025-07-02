package com.main.orderservice.payloads.cart;

import com.main.orderservice.payloads.product.ProductResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartItemDTO {
    private Long cartItemId;
    private CartDTO cart;
    private ProductResponse product;
    private Integer quantity;
    private Double discount;
    private Double price;
}
