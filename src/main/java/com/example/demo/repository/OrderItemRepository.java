package com.example.demo.repository;

import com.example.demo.model.OrderItem;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    List<OrderItem> findByOrderIdAndDeletedFalse(Long orderId);

    java.util.Optional<OrderItem> findByIdAndOrderIdAndDeletedFalse(Long id, Long orderId);
}
