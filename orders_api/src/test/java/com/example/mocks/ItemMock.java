package com.example.mocks;

import com.example.dto.ItemDto;

public final class ItemMock {
    public static final String id = "65c303d0e6e2b520c5064ff0";
    public static final String name = "Bag of mangos";
    public static final String category = "Produce";
    public static final Double price = 5.0;
    public static final ItemDto mockItem = new ItemDto(id, name, category, price);
}
