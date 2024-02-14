package com.example.service;

import com.example.dto.CustomerDto;
import com.example.entity.Customer;
import com.example.mapper.CustomerMapper;
import com.example.repository.CustomerRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static com.example.mocks.CustomerMock.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {

    @InjectMocks
    private CustomerService customerService;

    @Mock
    private CustomerRepository customerRepository;

    @Spy
    private CustomerMapper customerMapper = Mappers.getMapper(CustomerMapper.class);

    @Test
    void createCustomer() {
        final CustomerDto customerDto = new CustomerDto(null, name, email);
        final Customer customer = customerMapper.dtoToEntity(customerDto);
        final Customer savedCustomer = customerMapper.dtoToEntity(mockCustomer);
        doAnswer(invocation ->
                Mono.just(savedCustomer))
                .when(customerRepository).insert(eq(customer));

        customerService.createCustomer(customerDto);
        Mockito.verify(customerRepository, times(1)).insert(eq(customer));
    }

    @Test
    void fetchCustomers() {
        final Customer savedCustomer = customerMapper.dtoToEntity(mockCustomer);
        doAnswer(invocation ->
                Flux.just(savedCustomer))
                .when(customerRepository).findAll();

        customerService.fetchCustomers();
        Mockito.verify(customerRepository, times(1)).findAll();
    }
}