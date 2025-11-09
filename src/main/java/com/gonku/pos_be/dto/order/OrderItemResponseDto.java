package com.gonku.pos_be.dto.order;

import com.gonku.pos_be.entity.Order;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderItemResponseDto {
    private Long id;
    private Order order;
    private Long productId;
    private String productName;
    private BigDecimal price;
    private int quantity;
    private BigDecimal discount;
    private BigDecimal amount;
}
