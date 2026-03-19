package com.project.sales_api.dto;

public record CustomerResponseDTO(
        Long id,
        String name,
        String email,
        String document
) {}
