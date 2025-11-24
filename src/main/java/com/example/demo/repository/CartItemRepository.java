package com.example.demo.repository;

import com.example.demo.model.CartItem;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    Optional<CartItem> findByShoppingCartIdAndBookId(Long shoppingCartId, Long bookId);

    Page<CartItem> findByShoppingCartId(Long shoppingCartId, Pageable pageable);

    List<CartItem> findByShoppingCartId(Long cartId);
}
