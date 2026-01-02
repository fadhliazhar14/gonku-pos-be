package com.gonku.pos_be.dto.order;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderItemResponseDto {
    private Long id;
    private Long productId;
    private String productName;
    private BigDecimal price;
    private BigDecimal quantity;
    private BigDecimal discount;
    private BigDecimal amount;
}
