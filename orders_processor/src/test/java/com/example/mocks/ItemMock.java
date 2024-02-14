package com.example.mocks;

import com.example.entity.Item;

public final class ItemMock {
    public static final String id = "65c303d0e6e2b520c5064ff0";
    public static final String name = "Bag of mangos";
    public static final String category = "Produce";
    public static final Double price = 5.0;
    public static final String id2 = "65c303d0e6e2b520c5064ff2";
    public static final String name2 = "Half dozen of donuts";
    public static final String category2 = "Bakery";
    public static final Double price2 = 8.0;
    public static final Item mockItem = new Item(id, name, category, price);
    public static final Item mockItem2 = new Item(id2, name2, category2, price2);
}
