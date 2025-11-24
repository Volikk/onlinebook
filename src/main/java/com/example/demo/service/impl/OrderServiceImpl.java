package com.example.demo.service.impl;

import com.example.demo.dto.order.CreateOrderRequestDto;
import com.example.demo.dto.order.OrderItemResponseDto;
import com.example.demo.dto.order.OrderResponseDto;
import com.example.demo.dto.order.UpdateOrderStatusRequestDto;
import com.example.demo.entity.Order;
import com.example.demo.entity.enums.OrderStatus;
import com.example.demo.mapper.OrderMapper;
import com.example.demo.model.Book;
import com.example.demo.model.CartItem;
import com.example.demo.model.OrderItem;
import com.example.demo.model.ShoppingCart;
import com.example.demo.repository.BookRepository;
import com.example.demo.repository.CartItemRepository;
import com.example.demo.repository.OrderItemRepository;
import com.example.demo.repository.OrderRepository;
import com.example.demo.repository.ShoppingCartRepository;
import com.example.demo.service.OrderService;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final ShoppingCartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final BookRepository bookRepository;
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final OrderMapper mapper;

    @Transactional
    @Override
    public OrderResponseDto placeOrder(Long userId, CreateOrderRequestDto req) {
        ShoppingCart cart = cartRepository.findByUserIdAndDeletedFalse(userId)
                .orElseThrow(() -> new IllegalArgumentException("Cart not found for user "
                        + userId));

        List<CartItem> items = cartItemRepository.findByShoppingCartId(cart.getId());
        if (items.isEmpty()) {
            throw new IllegalStateException("Cart is empty");
        }

        Order order = new Order();
        order.setUser(cart.getUser());
        order.setStatus(OrderStatus.PENDING);
        order.setOrderDate(LocalDateTime.now());
        order.setShippingAddress(req.getShippingAddress());

        BigDecimal total = BigDecimal.ZERO;

        for (CartItem ci : items) {
            Book book = ci.getBook();
            OrderItem oi = new OrderItem();
            oi.setOrder(order);
            oi.setBook(book);
            oi.setQuantity(ci.getQuantity());
            oi.setPrice(book.getPrice());
            order.getOrderItems().add(oi);
            total = total.add(book.getPrice().multiply(BigDecimal.valueOf(ci.getQuantity())));
        }

        order.setTotal(total);
        order = orderRepository.save(order);

        cartItemRepository.deleteAll(items);

        return mapper.toDto(order);
    }

    @Transactional(readOnly = true)
    @Override
    public Page<OrderResponseDto> getOrdersForUser(Long userId, Pageable pageable) {
        Page<Order> page = orderRepository.findAllByUserIdAndDeletedFalse(userId, pageable);
        return page.map(mapper::toDto);
    }

    @Transactional(readOnly = true)
    @Override
    public OrderResponseDto getOrderById(Long userId, Long orderId) {
        Order order = orderRepository.findByIdAndDeletedFalse(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Order not found: " + orderId));
        if (!order.getUser().getId().equals(userId)) {
            throw new SecurityException("You cannot view another user's order");
        }
        return mapper.toDto(order);
    }

    @Transactional(readOnly = true)
    @Override
    public List<OrderItemResponseDto> getOrderItems(Long userId, Long orderId) {
        Order order = orderRepository.findByIdAndDeletedFalse(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Order not found: " + orderId));
        if (!order.getUser().getId().equals(userId)) {
            throw new SecurityException("You cannot view another user's order items");
        }
        return order.getOrderItems().stream()
                .filter(oi -> !oi.isDeleted())
                .map(mapper::toItemDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    @Override
    public OrderItemResponseDto getOrderItem(Long userId, Long orderId, Long itemId) {
        orderRepository.findByIdAndDeletedFalse(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Order not found: " + orderId));
        var itemOpt = orderItemRepository.findByIdAndOrderIdAndDeletedFalse(itemId, orderId);
        var item = itemOpt.orElseThrow(() -> new IllegalArgumentException("Order item not found: "
                + itemId));
        if (!item.getOrder().getUser().getId().equals(userId)) {
            throw new SecurityException("You cannot view another user's order item");
        }
        return mapper.toItemDto(item);
    }

    @Transactional
    @Override
    public OrderResponseDto updateOrderStatus(Long orderId, UpdateOrderStatusRequestDto req) {
        Order order = orderRepository.findByIdAndDeletedFalse(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Order not found: " + orderId));
        order.setStatus(req.getStatus());
        order = orderRepository.save(order);
        return mapper.toDto(order);
    }
}
