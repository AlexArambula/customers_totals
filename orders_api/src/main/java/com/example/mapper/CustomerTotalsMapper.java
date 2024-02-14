package com.example.mapper;

import com.example.dto.CustomerTotalsDto;
import com.example.entity.CustomerTotals;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CustomerTotalsMapper {

    CustomerTotals dtoToEntity(CustomerTotalsDto customerTotals);
    CustomerTotalsDto entityToDto(CustomerTotals customerTotals);
}
