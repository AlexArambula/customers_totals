package com.example.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Map;

@Document(collection = "customer_totals")
public record CustomerTotals(
        @Id
        String id,
        Customer customer,
        Map<String, CustomerTotals.CategoryTotal> totals

) {
    public record CategoryTotal(
            Long quantity,
            Double amount
    ) {
    }

    public CustomerTotals(Customer customer) {
        this(null, customer, null);
    }
}
