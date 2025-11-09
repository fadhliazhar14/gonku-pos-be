package com.gonku.pos_be.dto.order;

import com.gonku.pos_be.entity.Order;
import com.gonku.pos_be.entity.PaymentMethod;
import com.gonku.pos_be.entity.PaymentStatus;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class OrderPaymentResponseDto {
    private Long id;
    private Order order;
    private String paymentMethod;
    private BigDecimal amount;
    private PaymentStatus status;
    private String referenceNo;
    private String notes;
    private LocalDateTime createdAt;
}
