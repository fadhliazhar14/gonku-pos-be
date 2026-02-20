package com.gonku.pos_be.dto.order;

import com.gonku.pos_be.entity.payment.PaymentMethod;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderPaymentRequestDto {
    @NotNull(message = "Payment method is required")
    @NotBlank(message = "This field cannot be empty")
    @Size(max = 20)
    private PaymentMethod paymentMethod;

    @NotNull(message = "Amount is required")
    @Min(value = 0, message = "Change amount must be equal or greater than 0")
    private BigDecimal amount;

    @Size(max = 50, message = "Notes max 50 characters")
    private String notes;
}
