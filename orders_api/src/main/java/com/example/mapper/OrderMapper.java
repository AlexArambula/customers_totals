package com.example.mapper;

import com.example.dto.OrderDto;
import com.example.entity.Order;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface OrderMapper {
    Order dtoToEntity(OrderDto order);
    OrderDto entityToDto(Order order);
}
