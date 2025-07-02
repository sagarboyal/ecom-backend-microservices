package com.main.orderservice.controller;


import com.main.orderservice.payloads.order.OrderDTO;
import com.main.orderservice.payloads.order.OrderRequestDTO;
import com.main.orderservice.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/order")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @PostMapping("/users/payments/{paymentMethod}/{email}")
    public ResponseEntity<OrderDTO> orderProducts(@PathVariable String paymentMethod,
                                                  @PathVariable String email,
                                                  @RequestBody OrderRequestDTO orderRequestDTO) {
        OrderDTO order = orderService.placeOrder(
                email,
                orderRequestDTO.getAddressId(),
                paymentMethod,
                orderRequestDTO.getPaymentGatewayName(),
                orderRequestDTO.getPaymentGatewayId(),
                orderRequestDTO.getPaymentGatewayStatus(),
                orderRequestDTO.getPaymentGatewayResponseMessage()
        );
        return new ResponseEntity<>(order, HttpStatus.CREATED);
    }
}

