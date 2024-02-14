package com.example.mapper;

import com.example.dto.CustomerDto;
import com.example.entity.Customer;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CustomerMapper {

    Customer dtoToEntity(CustomerDto customer);
    CustomerDto entityToDto(Customer customer);
}
