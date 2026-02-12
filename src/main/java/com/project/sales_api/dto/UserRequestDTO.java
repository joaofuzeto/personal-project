package com.project.sales_api.dto;

import com.project.sales_api.Enums.Roles;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UserRequestDTO(

        @NotBlank(message = "Nome do usuário é obrigatório")
        String name,

        @Email
        @NotBlank
        String email,

        @NotBlank
        String password,

        @NotBlank
        Roles role) {
}
