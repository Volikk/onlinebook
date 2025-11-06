package com.example.demo.controller;

import com.example.demo.dto.cart.AddCartItemRequestDto;
import com.example.demo.dto.cart.ShoppingCartResponseDto;
import com.example.demo.dto.cart.UpdateCartItemRequestDto;
import com.example.demo.entity.User;
import com.example.demo.service.ShoppingCartService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class ShoppingCartController {
    private final ShoppingCartService cartService;

    @Operation(summary = "Отримати кошик користувача")
    @GetMapping
    public ShoppingCartResponseDto getCart(@AuthenticationPrincipal User user) {
        return cartService.getCartForUser(user.getId());
    }

    @Operation(summary = "Додати книгу до кошика")
    @PostMapping
    public ShoppingCartResponseDto addBook(
            @AuthenticationPrincipal User user,
            @Valid @RequestBody AddCartItemRequestDto request) {
        return cartService.addBookToCart(user.getId(), request);
    }

    @Operation(summary = "Оновити кількість книги в кошику")
    @PutMapping("/items/{cartItemId}")
    public ShoppingCartResponseDto updateItem(
            @AuthenticationPrincipal User user,
            @PathVariable Long cartItemId,
            @Valid @RequestBody UpdateCartItemRequestDto request) {
        return cartService.updateCartItem(user.getId(), cartItemId, request);
    }

    @Operation(summary = "Видалити книгу з кошика")
    @DeleteMapping("/items/{cartItemId}")
    public void deleteItem(
            @AuthenticationPrincipal User user,
            @PathVariable Long cartItemId) {
        cartService.removeCartItem(user.getId(), cartItemId);
    }
}
