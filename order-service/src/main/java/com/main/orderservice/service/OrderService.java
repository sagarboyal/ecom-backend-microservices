package com.main.orderservice.service;

import com.main.orderservice.payloads.order.OrderDTO;
import com.main.orderservice.payloads.order.OrderRequestDTO;

public interface OrderService {
    OrderDTO placeOrder(OrderRequestDTO orderRequestDTO);
}
