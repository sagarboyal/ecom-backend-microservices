package com.main.paymentservice.serviceImpl;

import com.main.paymentservice.exceptions.custom.ResourceNotFoundException;
import com.main.paymentservice.model.Payment;
import com.main.paymentservice.model.PaymentStatus;
import com.main.paymentservice.payload.PaymentDTO;
import com.main.paymentservice.repository.PaymentRepository;
import com.main.paymentservice.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {
    private final PaymentRepository paymentRepository;

    @Override
    public PaymentDTO createPayment(Long orderId, PaymentDTO paymentDTO) {
        Payment payment = convertPaymentDTOtoEntity(paymentDTO);
        payment.setOrderId(orderId);
        return convertEntityToPaymentDTO(paymentRepository.save(payment));
    }

    @Override
    public PaymentDTO updatePayment(PaymentDTO paymentDTO) {
        Payment payment = getById(paymentDTO.getPaymentId());

        payment.setPaymentMethod(paymentDTO.getPaymentMethod() != null
                && !paymentDTO.getPaymentMethod().trim().isEmpty()
                ? paymentDTO.getPaymentMethod()
                : payment.getPaymentMethod());

        payment.setPaymentGatewayId(paymentDTO.getPaymentGatewayId() != null
        && !paymentDTO.getPaymentGatewayId().trim().isEmpty() ?
                paymentDTO.getPaymentGatewayId() :  payment.getPaymentGatewayId()
        );

        payment.setPaymentGatewayName(paymentDTO.getPaymentGatewayName() != null
                    && !paymentDTO.getPaymentGatewayName().trim().isEmpty() ?
                paymentDTO.getPaymentGatewayName() :  payment.getPaymentGatewayName()

        );

        payment.setPaymentGatewayStatus(paymentDTO.getPaymentGatewayStatus() != null
                && !paymentDTO.getPaymentGatewayStatus().trim().isEmpty() ?
                PaymentStatus.valueOf(paymentDTO.getPaymentGatewayName().toUpperCase())
                :  payment.getPaymentGatewayStatus()

        );

        payment.setPaymentGatewayResponseMessage(paymentDTO.getPaymentGatewayResponseMessage() != null
                && !paymentDTO.getPaymentGatewayResponseMessage().trim().isEmpty() ?
                paymentDTO.getPaymentGatewayResponseMessage()
                :  payment.getPaymentGatewayResponseMessage()

        );
        return convertEntityToPaymentDTO(paymentRepository.save(payment));
    }

    @Override
    public void deletePayment(String paymentId) {
        Payment payment = getById(paymentId);
        paymentRepository.delete(payment);
    }

    @Override
    public List<PaymentDTO> getPayments() {
        return paymentRepository.findAll().stream()
                .map(this::convertEntityToPaymentDTO)
                .collect(Collectors.toList());
    }

    @Override
    public PaymentDTO getPaymentsById(String paymentId) {
        Payment payment = paymentRepository.findByPaymentId(paymentId)
                .orElseThrow(() -> new ResourceNotFoundException(Payment.class.getName(), "paymentId", paymentId));
        return convertEntityToPaymentDTO(payment);
    }

    private Payment convertPaymentDTOtoEntity(PaymentDTO paymentDTO) {
        Payment payment = new Payment(paymentDTO.getPaymentMethod(),
                paymentDTO.getPaymentGatewayId(),
                paymentDTO.getPaymentGatewayName(),
                PaymentStatus.valueOf(paymentDTO.getPaymentGatewayStatus()),
                paymentDTO.getPaymentGatewayResponseMessage());
        if (paymentDTO.getPaymentId() != null && !paymentDTO.getPaymentId().trim().isEmpty()) {
            payment.setPaymentId(paymentDTO.getPaymentId());
        }
        return payment;
    }

    private PaymentDTO convertEntityToPaymentDTO(Payment payment) {
        return PaymentDTO.builder()
                .paymentId(payment.getPaymentId())
                .paymentGatewayId(payment.getPaymentGatewayId())
                .paymentMethod(payment.getPaymentMethod())
                .paymentGatewayName(payment.getPaymentGatewayName())
                .paymentGatewayStatus(payment.getPaymentGatewayStatus().toString())
                .paymentGatewayResponseMessage(payment.getPaymentGatewayResponseMessage())
                .build();
    }

    private Payment getById(String paymentId) {
        return paymentRepository.findByPaymentId(paymentId)
                .orElseThrow(() -> new ResourceNotFoundException(Payment.class.getName(), "paymentId", paymentId));
    }
}
