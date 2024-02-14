package com.example.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

import java.time.Instant;
import java.util.List;

public record OrderDto(
        String id,
        @NotEmpty
        CustomerDto customer,
        @Size(min = 1)
        List<ItemDto> items,
        Instant createdAt,
        Instant lastModifiedAt,
        Status status
) {
        public enum Status {
                CREATED, CANCELED
        }
}
