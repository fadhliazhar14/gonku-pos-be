package com.gonku.pos_be.dto.order;

import com.gonku.pos_be.entity.order.OrderPayment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface OrderPaymentMapper {
    OrderPaymentResponseDto toDto(OrderPayment orderPayment);

    List<OrderPaymentResponseDto> toDtoList(List<OrderPayment> orderPayments);

    @Mapping(target = "id", ignore = true)
    OrderPayment toEntity(OrderPaymentRequestDto requestDto);
}
