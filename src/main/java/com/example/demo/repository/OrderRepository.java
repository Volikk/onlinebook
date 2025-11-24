package com.example.demo.repository;

import com.example.demo.entity.Order;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
    Page<Order> findAllByUserIdAndDeletedFalse(Long userId, Pageable pageable);

    List<Order> findAllByUserIdAndDeletedFalse(Long userId);

    java.util.Optional<Order> findByIdAndDeletedFalse(Long id);
}
