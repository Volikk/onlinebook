package com.example.demo.service;

import com.example.demo.dto.order.CreateOrderRequestDto;
import com.example.demo.dto.order.OrderItemResponseDto;
import com.example.demo.dto.order.OrderResponseDto;
import com.example.demo.dto.order.UpdateOrderStatusRequestDto;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OrderService {
    OrderResponseDto placeOrder(Long userId, CreateOrderRequestDto req);

    Page<OrderResponseDto> getOrdersForUser(Long userId, Pageable pageable);

    List<OrderItemResponseDto> getOrderItems(Long userId, Long orderId);

    OrderItemResponseDto getOrderItem(Long userId, Long orderId, Long itemId);

    OrderResponseDto updateOrderStatus(Long orderId, UpdateOrderStatusRequestDto req);
}
