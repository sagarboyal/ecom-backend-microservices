package com.main.paymentservice.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "payments")
public class Payment {

    @Id
    private String paymentId;

    @NotNull(message = "Order ID must not be null")
    private Long orderId;

    @NotBlank
    @Size(min = 4, message = "Payment method must contain at least 4 characters")
    private String paymentMethod;

    private String paymentGatewayId;
    private String paymentGatewayName;

    @NotBlank(message = "Payment gateway status must not be blank")
    private PaymentStatus paymentGatewayStatus;

    private String paymentGatewayResponseMessage;

    private LocalDateTime createdAt = LocalDateTime.now();
    private LocalDateTime updatedAt = LocalDateTime.now();

    public Payment(String paymentMethod, String paymentGatewayId, String paymentGatewayName, PaymentStatus paymentGatewayStatus, String paymentGatewayResponseMessage) {
        this.paymentMethod = paymentMethod;
        this.paymentGatewayId = paymentGatewayId;
        this.paymentGatewayName = paymentGatewayName;
        this.paymentGatewayStatus = paymentGatewayStatus;
        this.paymentGatewayResponseMessage = paymentGatewayResponseMessage;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
}
