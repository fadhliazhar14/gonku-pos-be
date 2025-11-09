package com.gonku.pos_be.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PaymentMethod {
    CASH("Cash"),
    DEBIT_CARD("Debit Card"),
    CREDIT_CARD("Credit Card"),
    BANK_TRANSFER("Bank Transfer"),
    QRIS("QRIS"),
    EWALLET("E-Wallet"),
    VOUCHER("Voucher"),
    POINTS("Points"),
    MIXED("Mixed"),
    OTHER("Other");

    private final String label;
}

