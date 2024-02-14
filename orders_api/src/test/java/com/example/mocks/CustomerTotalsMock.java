package com.example.mocks;

import com.example.entity.Customer;
import com.example.entity.CustomerTotals;

import java.util.Map;

import static com.example.mocks.CustomerMock.mockCustomer;
import static com.example.mocks.ItemMock.mockItem;

public final class CustomerTotalsMock {
    public static final String id = "65c303d0e6e2b520c5064ff4";
    public static final CustomerTotals mockCustomerTotals = new CustomerTotals(id, new Customer(mockCustomer.id()),
            Map.of(mockItem.category(), new CustomerTotals.CategoryTotal(1L, mockItem.price())));
}
