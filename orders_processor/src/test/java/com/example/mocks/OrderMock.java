package com.example.mocks;

import com.example.entity.Order;

import java.time.Instant;
import java.util.List;

import static com.example.mocks.CustomerMock.mockCustomer;
import static com.example.mocks.ItemMock.mockItem;
import static com.example.mocks.ItemMock.mockItem2;

public final class OrderMock {
    public static final String id = "65c303d0e6e2b520c5064ff5";
    public static final Instant createdAt = Instant.parse("2024-01-30T20:30:00.000Z");
    public static final Instant lastModifiedAt = Instant.parse("2024-01-30T20:30:00.000Z");
    public static final Order mockOrder = Order.builder()
            .id(id)
            .customer(mockCustomer)
            .items(List.of(mockItem))
            .createdAt(createdAt)
            .lastModifiedAt(lastModifiedAt)
            .status(Order.Status.CREATED)
            .build();

    public static final Order mockOrder2Items = Order.builder()
            .id(id)
            .customer(mockCustomer)
            .items(List.of(mockItem, mockItem2))
            .createdAt(createdAt)
            .lastModifiedAt(lastModifiedAt)
            .status(Order.Status.CREATED)
            .build();
}
