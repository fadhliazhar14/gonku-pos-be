package com.gonku.pos_be.entity.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SubscriptionPlan {
    FREE("FREE"),
    MONTHLY("MONTHLY"),
    YEARLY("YEARLY");

    private final String label;
}
