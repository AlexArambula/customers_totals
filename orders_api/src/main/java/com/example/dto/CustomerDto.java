package com.example.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

public record CustomerDto(
        String id,
        @Size(min = 1, max = 255)
        String name,
        @Email
        String email
) {
}
