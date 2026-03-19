package com.project.sales_api.dto;

import com.project.sales_api.Enums.Roles;

public record UserResponseDTO(
        Long id,
        String name,
        String email,
        Roles role
) {
}
