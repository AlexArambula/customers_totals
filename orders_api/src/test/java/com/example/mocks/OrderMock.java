package com.example.mocks;

import com.example.dto.OrderDto;

import java.time.Instant;
import java.util.List;

import static com.example.mocks.CustomerMock.mockCustomer;
import static com.example.mocks.ItemMock.mockItem;

public final class OrderMock {
    public static final String id = "65c303d0e6e2b520c5064ff5";
    public static final Instant createdAt = Instant.parse("2024-01-30T20:30:00.000Z");
    public static final Instant lastModifiedAt = Instant.parse("2024-01-30T20:30:00.000Z");
    public static final OrderDto mockOrder =
            new OrderDto(id, mockCustomer, List.of(mockItem), createdAt, lastModifiedAt, OrderDto.Status.CREATED);
}
