package com.example.demo.controller;

import com.example.demo.dto.order.CreateOrderRequestDto;
import com.example.demo.dto.order.OrderItemResponseDto;
import com.example.demo.dto.order.OrderResponseDto;
import com.example.demo.dto.order.UpdateOrderStatusRequestDto;
import com.example.demo.entity.User;
import com.example.demo.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Order Management", description = "Endpoints for managing orders")
@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @Operation(summary = "Place an order", description = "Create a new order from shopping cart")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('USER')")
    public OrderResponseDto placeOrder(
            @AuthenticationPrincipal User user,
            @Valid @RequestBody CreateOrderRequestDto request) {
        return orderService.placeOrder(user.getId(), request);
    }

    @Operation(summary = "Get orders for current user",
            description = "Retrieve order history for user")
    @GetMapping
    @PreAuthorize("hasRole('USER')")
    public Page<OrderResponseDto> getOrders(
            @AuthenticationPrincipal User user,
            Pageable pageable) {
        return orderService.getOrdersForUser(user.getId(), pageable);
    }

    @Operation(summary = "Update order status",
            description = "Admin only endpoint to change order status")
    @PatchMapping("/{orderId}")
    @PreAuthorize("hasRole('ADMIN')")
    public OrderResponseDto updateStatus(
            @PathVariable Long orderId,
            @Valid @RequestBody UpdateOrderStatusRequestDto request) {
        return orderService.updateOrderStatus(orderId, request);
    }

    @Operation(summary = "Get items for an order",
            description = "Retrieve all items within a specific order")
    @GetMapping("/{orderId}/items")
    @PreAuthorize("hasRole('USER')")
    public List<OrderItemResponseDto> getOrderItems(
            @AuthenticationPrincipal User user,
            @PathVariable Long orderId) {
        return orderService.getOrderItems(user.getId(), orderId);
    }

    @Operation(summary = "Get a specific item from an order",
            description = "Retrieve details of a single order item")
    @GetMapping("/{orderId}/items/{itemId}")
    @PreAuthorize("hasRole('USER')")
    public OrderItemResponseDto getOrderItem(
            @AuthenticationPrincipal User user,
            @PathVariable Long orderId,
            @PathVariable Long itemId) {
        return orderService.getOrderItem(user.getId(), orderId, itemId);
    }
}
