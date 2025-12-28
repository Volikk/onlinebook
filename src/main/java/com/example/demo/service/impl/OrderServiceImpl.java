package com.example.demo.service.impl;

import com.example.demo.dto.order.CreateOrderRequestDto;
import com.example.demo.dto.order.OrderItemResponseDto;
import com.example.demo.dto.order.OrderResponseDto;
import com.example.demo.dto.order.UpdateOrderStatusRequestDto;
import com.example.demo.entity.Order;
import com.example.demo.entity.enums.OrderStatus;
import com.example.demo.exception.EntityNotFoundException;
import com.example.demo.exception.OrderProcessingException;
import com.example.demo.mapper.OrderItemMapper;
import com.example.demo.mapper.OrderMapper;
import com.example.demo.model.CartItem;
import com.example.demo.model.OrderItem;
import com.example.demo.model.ShoppingCart;
import com.example.demo.repository.CartItemRepository;
import com.example.demo.repository.OrderItemRepository;
import com.example.demo.repository.OrderRepository;
import com.example.demo.repository.ShoppingCartRepository;
import com.example.demo.service.OrderService;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final ShoppingCartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final OrderMapper orderMapper;
    private final OrderItemMapper orderItemMapper;

    @Transactional
    @Override
    public OrderResponseDto placeOrder(Long userId, CreateOrderRequestDto requestDto) {
        ShoppingCart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Can't find shopping cart for user with id: " + userId)
                );

        Set<CartItem> cartItems = cart.getCartItems();
        if (cartItems.isEmpty()) {
            throw new OrderProcessingException(
                    "Can't place order: shopping cart is empty for user with id: " + userId)
                    ;
        }

        Order order = new Order();
        order.setUser(cart.getUser());
        order.setStatus(OrderStatus.PENDING);
        order.setOrderDate(LocalDateTime.now());
        order.setShippingAddress(requestDto.getShippingAddress());

        BigDecimal total = BigDecimal.ZERO;
        for (CartItem item : cartItems) {
            OrderItem orderItem = orderItemMapper.toEntity(item);
            orderItem.setOrder(order);
            orderItem.setPrice(item.getBook().getPrice());

            order.getOrderItems().add(orderItem);

            BigDecimal itemTotal = item.getBook().getPrice()
                    .multiply(BigDecimal.valueOf(item.getQuantity()));
            total = total.add(itemTotal);
        }

        order.setTotal(total);
        orderRepository.save(order);

        cartItemRepository.deleteAll(cartItems);

        return orderMapper.toDto(order);
    }

    @Transactional(readOnly = true)
    @Override
    public Page<OrderResponseDto> getOrdersForUser(Long userId, Pageable pageable) {
        return orderRepository.findAllByUserId(userId, pageable)
                .map(orderMapper::toDto);
    }

    @Transactional
    @Override
    public OrderResponseDto updateOrderStatus(
            Long orderId, UpdateOrderStatusRequestDto requestDto
    ) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Can't find order by id: " + orderId
                ));

        order.setStatus(requestDto.getStatus());
        return orderMapper.toDto(orderRepository.save(order));
    }

    @Transactional(readOnly = true)
    @Override
    public List<OrderItemResponseDto> getOrderItems(Long userId, Long orderId) {
        Order order = orderRepository.findByIdAndUserId(orderId, userId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Can't find order by id: " + orderId + " for user with id: " + userId
                ));

        return order.getOrderItems().stream()
                .map(orderItemMapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    @Override
    public OrderItemResponseDto getOrderItem(Long userId, Long orderId, Long itemId) {
        return orderItemRepository.findByIdAndOrderIdAndOrderUserId(itemId, orderId, userId)
                .map(orderItemMapper::toDto)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Can't find order item by id: " + itemId
                                + " in order with id: " + orderId + " for user with id: " + userId)
                );
    }
}
