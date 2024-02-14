package com.example.mapper;

import com.example.dto.ItemDto;
import com.example.entity.Item;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ItemMapper {
    Item dtoToEntity(ItemDto item);
    ItemDto entityToDto(Item item);
}
