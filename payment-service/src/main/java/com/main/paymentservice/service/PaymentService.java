package com.main.paymentservice.service;

import com.main.paymentservice.payload.PaymentDTO;

import java.util.List;

public interface PaymentService {
    PaymentDTO createPayment(Long orderId, PaymentDTO paymentDTO);
    PaymentDTO updatePayment(PaymentDTO paymentDTO);
    void  deletePayment(String paymentId);
    List<PaymentDTO> getPayments();
    PaymentDTO getPaymentsById(String paymentId);
}
