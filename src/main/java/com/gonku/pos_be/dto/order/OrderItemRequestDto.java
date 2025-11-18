package com.gonku.pos_be.dto.order;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderItemRequestDto {
    @NotNull(message = "Product ID is required")
    @Min(value = 1, message = "Product ID must be at least 1")
    private Long productId;

    @Min(value = 1, message = "Quantity must be equal or greater than 0")
    private BigDecimal quantity;

    @Min(value = 0, message = "Discount must be equal or greater than 0")
    private BigDecimal discount;
}
