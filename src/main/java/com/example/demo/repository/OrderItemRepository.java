package com.example.demo.repository;

import com.example.demo.model.OrderItem;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    List<OrderItem> findByOrderId(Long orderId);

    Optional<OrderItem> findByIdAndOrderIdAndOrderUserId(Long itemId, Long orderId, Long userId);
}
