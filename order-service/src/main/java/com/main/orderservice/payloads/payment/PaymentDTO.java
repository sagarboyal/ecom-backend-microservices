package com.main.orderservice.payloads.payment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentDTO {
    private String paymentId;
    private String paymentMethod;
    private String paymentGatewayId;
    private String paymentGatewayName;
    private String paymentGatewayStatus;
    private String paymentGatewayResponseMessage;
}
