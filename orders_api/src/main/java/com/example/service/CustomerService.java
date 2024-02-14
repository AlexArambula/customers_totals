package com.example.service;

import com.example.dto.CustomerDto;
import com.example.entity.Customer;
import com.example.mapper.CustomerMapper;
import com.example.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;

    public Mono<CustomerDto> createCustomer(CustomerDto customer) {
        Mono<Customer> customerEntity = customerRepository.insert(customerMapper.dtoToEntity(customer));
        return customerEntity.map(customerMapper::entityToDto);
    }

    public Flux<CustomerDto> fetchCustomers() {
        return customerRepository.findAll()
                .map(customerMapper::entityToDto);
    }
}
