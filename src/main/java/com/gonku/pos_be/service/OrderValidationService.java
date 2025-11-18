package com.gonku.pos_be.service;

import com.gonku.pos_be.entity.OrderStatus;
import com.gonku.pos_be.exception.BusinessValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class OrderValidationService {
    private static final Set<OrderStatus> INVALID_CANCEL_STATUS = Set.of(
            OrderStatus.NEW,
            OrderStatus.CANCELLED,
            OrderStatus.FAILED,
            OrderStatus.REFUNDED,
            OrderStatus.VOIDED
    );

    public void validateCancelable(OrderStatus status) {
        if (INVALID_CANCEL_STATUS.contains(status)) {
            throw new BusinessValidationException(
                    "Order can not be cancelled or voided. Current status: " + status
            );
        }
    }
}
