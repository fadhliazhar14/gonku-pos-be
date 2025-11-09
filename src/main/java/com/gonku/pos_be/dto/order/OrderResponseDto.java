package com.gonku.pos_be.dto.order;

import com.gonku.pos_be.entity.OrderStatus;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
public class OrderResponseDto {
    private Long id;
    private String receiptNumber;
    private LocalDate orderDate;
    private OrderStatus status;
    private String outletName;
    private String customerName;
    private String tableNumber;
    private BigDecimal discountTotal;
    private BigDecimal subtotal;
    private BigDecimal taxTotal;
    private BigDecimal totalAmount;
    private BigDecimal paidAmount;
    private BigDecimal changeAmount;
    private String notes;
}
