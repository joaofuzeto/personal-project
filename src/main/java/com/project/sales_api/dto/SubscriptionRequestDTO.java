package com.project.sales_api.dto;

import com.project.sales_api.Enums.SubscriptionStatus;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record SubscriptionRequestDTO(

        @NotBlank(message = "A assinatura deve ter um nome")
        String planName,

        @NotNull(message = "O preço é obrigatório")
        @DecimalMin(value = "0.00", inclusive = false,  message = "Preço deve ser maior que zero")
        BigDecimal price,

        @NotNull(message = "Informe o id do Cliente associado a esse serviço")
        Long customerId
) {
}
