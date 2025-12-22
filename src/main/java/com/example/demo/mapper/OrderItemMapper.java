package com.example.demo.mapper;

import com.example.demo.dto.order.OrderItemResponseDto;
import com.example.demo.model.CartItem;
import com.example.demo.model.OrderItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface OrderItemMapper {

    @Mapping(target = "bookId", source = "book.id")
    @Mapping(target = "bookTitle", source = "book.title")
    OrderItemResponseDto toDto(OrderItem orderItem);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "order", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    @Mapping(target = "book", source = "book")
    @Mapping(target = "price", source = "book.price")
    OrderItem toEntity(CartItem cartItem);
}
