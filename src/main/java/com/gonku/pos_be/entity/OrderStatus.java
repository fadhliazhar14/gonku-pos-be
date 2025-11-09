package com.gonku.pos_be.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum OrderStatus {
    NEW("New"),
    ON_HOLD("On Hold"),
    UNPAID("Unpaid"),
    PAID("Paid"),
    COMPLETED("Completed"),
    CANCELLED("Cancelled"),
    VOIDED("Voided"),
    REFUNDED("Refunded"),
    FAILED("Failed");

    private final String label;
}

