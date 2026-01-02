package com.gonku.pos_be.dto.order;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class OrderDetailsResponseDto {
    private OrderResponseDto order;
    private List<OrderItemResponseDto> orderItems;
    private List<OrderPaymentResponseDto> orderPayments;
}
