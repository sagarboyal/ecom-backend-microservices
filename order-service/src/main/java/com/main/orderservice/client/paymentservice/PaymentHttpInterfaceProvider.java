package com.main.orderservice.client.paymentservice;

import com.main.orderservice.payloads.payment.PaymentDTO;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

@HttpExchange
public interface PaymentHttpInterfaceProvider {
    @GetExchange("/api/payment/{paymentId}")
    PaymentDTO getPaymentById(@PathVariable("paymentId") String paymentId);
    @PostExchange("/api/payment/order/{orderId}")
    PaymentDTO savePayment(@PathVariable Long orderId, @RequestBody PaymentDTO paymentDTO);
}
