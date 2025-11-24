package com.example.demo.mapper;

import com.example.demo.dto.order.OrderItemResponseDto;
import com.example.demo.dto.order.OrderResponseDto;
import com.example.demo.entity.Order;
import com.example.demo.model.OrderItem;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "orderItems", source = "orderItems")
    OrderResponseDto toDto(Order order);

    List<OrderResponseDto> toDtoList(List<Order> orders);

    @Mapping(target = "bookId", source = "book.id")
    @Mapping(target = "bookTitle", source = "book.title")
    OrderItemResponseDto toItemDto(OrderItem item);

    List<OrderItemResponseDto> toItemDtoList(List<OrderItem> items);
}
