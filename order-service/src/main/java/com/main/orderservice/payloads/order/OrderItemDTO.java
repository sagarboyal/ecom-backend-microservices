package com.main.orderservice.payloads.order;

import com.main.orderservice.payloads.product.ProductResponse;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OrderItemDTO {
    private Long orderItemId;
    private String productId;
    private Integer quantity;
    private double discount;
    private double orderedProductPrice;
}
