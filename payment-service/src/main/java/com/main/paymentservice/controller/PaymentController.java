package com.main.paymentservice.controller;

import com.main.paymentservice.payload.PaymentDTO;
import com.main.paymentservice.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/payment")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/order/{orderId}")
    public ResponseEntity<PaymentDTO> createPayment(@PathVariable Long orderId, @RequestBody PaymentDTO paymentDTO) {
        PaymentDTO createdPayment = paymentService.createPayment(orderId, paymentDTO);
        return ResponseEntity.ok(createdPayment);
    }

    @PutMapping
    public ResponseEntity<PaymentDTO> updatePayment(@RequestBody PaymentDTO paymentDTO) {
        PaymentDTO updatedPayment = paymentService.updatePayment(paymentDTO);
        return ResponseEntity.ok(updatedPayment);
    }

    @DeleteMapping("/{paymentId}")
    public ResponseEntity<String> deletePayment(@PathVariable String paymentId) {
        paymentService.deletePayment(paymentId);
        return ResponseEntity.ok("Payment deleted successfully");
    }

    @GetMapping
    public ResponseEntity<List<PaymentDTO>> getPayments() {
        List<PaymentDTO> payments = paymentService.getPayments();
        return ResponseEntity.ok(payments);
    }

    @GetMapping("/{paymentId}")
    public ResponseEntity<PaymentDTO> getPaymentById(@PathVariable String paymentId) {
        PaymentDTO payment = paymentService.getPaymentsById(paymentId);
        return ResponseEntity.ok(payment);
    }
}

