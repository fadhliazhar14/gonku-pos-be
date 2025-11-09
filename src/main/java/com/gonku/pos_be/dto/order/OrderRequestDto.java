package com.gonku.pos_be.dto.order;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class OrderRequestDto {
    @Size(min = 3, max = 50, message = "Outlet name must be between 3 and 50 characters")
    private String outletName;

    @NotBlank(message = "Customer name is required")
    @Size(min = 3, max = 50, message = "Customer name must be between 3 and 50 characters")
    private String customerName;

    private String tableNumber;

    @Min(value = 0, message = "Discount total must be equal or greater than 0")
    private BigDecimal discountTotal;

    @Size(max = 50, message = "Notes max 50 characters")
    private String notes;

    private List<OrderItemRequestDto> items;

    private List<OrderPaymentRequestDto> payments;
}
