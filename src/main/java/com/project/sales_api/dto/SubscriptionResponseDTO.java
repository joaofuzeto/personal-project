package com.project.sales_api.dto;

import com.project.sales_api.Enums.SubscriptionStatus;

import java.math.BigDecimal;
import java.time.LocalDate;

public record SubscriptionResponseDTO(
        Long id,
        String planName,
        BigDecimal price,
        SubscriptionStatus subscriptionStatus,
        String customerName,
        LocalDate startDate,
        LocalDate endDate
) {
}
