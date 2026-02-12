package com.project.sales_api.dto;

import com.project.sales_api.Enums.SubscriptionStatus;

import java.math.BigDecimal;

public record SubscriptionRequestDTO(
        String planName,
        BigDecimal price,
        SubscriptionStatus subscriptionStatus,
        Long customerId
) {
}
