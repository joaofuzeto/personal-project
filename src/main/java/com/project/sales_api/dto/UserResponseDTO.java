package com.project.sales_api.dto;

import com.project.sales_api.Enums.Roles;

public record UserResponseDTO(
        String name,
        String email,
        Roles role
) {
}
