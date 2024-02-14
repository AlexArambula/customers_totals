package com.example.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "customers")
public record Customer(
        @Id
        String id,
        String name,
        String email
) {
        public Customer(String id) {
                this(id, null, null);
        }
}
