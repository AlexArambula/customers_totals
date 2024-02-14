package com.example.dto;

import org.springframework.data.annotation.Id;
import java.util.Map;

public record CustomerTotalsDto(
        @Id
        String id,
        CustomerDto customer,
        Map<String, CategoryTotal> totals
) {
     public record CategoryTotal(
             Long quantity,
             Double amount
     ) {
     }
}
