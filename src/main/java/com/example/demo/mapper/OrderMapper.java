package com.example.demo.mapper;

import com.example.demo.dto.order.OrderResponseDto;
import com.example.demo.entity.Order;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = OrderItemMapper.class)
public interface OrderMapper {
    @Mapping(target = "userId", source = "user.id")
    OrderResponseDto toDto(Order order);

    List<OrderResponseDto> toDtoList(List<Order> orders);
}
