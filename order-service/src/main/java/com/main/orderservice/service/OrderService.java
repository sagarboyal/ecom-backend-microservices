package com.main.orderservice.service;

import com.main.orderservice.payloads.order.OrderDTO;

public interface OrderService {
    OrderDTO placeOrder(String emailId, Long addressId, String paymentMethod,
                        String paymentGatewayName, String paymentGatewayId, String paymentGatewayStatus,
                        String paymentGatewayResponseMessage);
}
