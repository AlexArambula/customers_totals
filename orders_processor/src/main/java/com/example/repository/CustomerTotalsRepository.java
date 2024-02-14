package com.example.repository;

import com.example.entity.CustomerTotals;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerTotalsRepository extends ReactiveMongoRepository<CustomerTotals, String> {
}
