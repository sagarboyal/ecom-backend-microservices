package com.main.orderservice.payloads.cart;

import com.main.orderservice.payloads.product.ProductResponse;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class CartDTO {
    private Long userId;
    private Long cartId;
    private Double totalPrice;
    private List<ProductResponse> products;
}
