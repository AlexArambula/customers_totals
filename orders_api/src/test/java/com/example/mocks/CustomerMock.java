package com.example.mocks;

import com.example.dto.CustomerDto;

public final class CustomerMock {
    public static final String id = "65c30003e6e2b520c5064d58";
    public static final String name = "John Doe";
    public static final String email = "doe.john@fakemail.net";
    public static final CustomerDto mockCustomer = new CustomerDto(id, name, email);
}
