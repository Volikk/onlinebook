package com.example.demo.service;

import com.example.demo.dto.cart.AddCartItemRequestDto;
import com.example.demo.dto.cart.CartItemResponseDto;
import com.example.demo.dto.cart.ShoppingCartResponseDto;
import com.example.demo.dto.cart.UpdateCartItemRequestDto;
import com.example.demo.exception.EntityNotFoundException;
import com.example.demo.model.Book;
import com.example.demo.model.CartItem;
import com.example.demo.model.ShoppingCart;
import com.example.demo.repository.BookRepository;
import com.example.demo.repository.CartItemRepository;
import com.example.demo.repository.ShoppingCartRepository;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ShoppingCartService {
    private final ShoppingCartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final BookRepository bookRepository;

    @Transactional(readOnly = true)
    public ShoppingCartResponseDto getCartForUser(Long userId) {
        ShoppingCart cart = cartRepository.findByUserIdWithItemsAndBooks(userId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Cart not found for user " + userId));
        return mapToResponse(cart);
    }

    @Transactional
    public ShoppingCartResponseDto addBookToCart(Long userId, AddCartItemRequestDto req) {
        ShoppingCart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Cart not found for user " + userId));

        Book book = bookRepository.findById(req.getBookId())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Book not found: " + req.getBookId()));

        CartItem item = cartItemRepository.findByShoppingCartIdAndBookId(cart.getId(), book.getId())
                .orElseGet(() -> {
                    CartItem ci = new CartItem();
                    ci.setShoppingCart(cart);
                    ci.setBook(book);
                    ci.setQuantity(0);
                    cart.getCartItems().add(ci);
                    return ci;
                });

        item.setQuantity(item.getQuantity() + req.getQuantity());
        cartRepository.save(cart);
        return mapToResponse(cart);
    }

    @Transactional
    public ShoppingCartResponseDto updateCartItem(Long userId,
                                                  Long cartItemId,
                                                  UpdateCartItemRequestDto req) {
        CartItem item = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Cart item not found: " + cartItemId));

        if (!item.getShoppingCart().getUser().getId().equals(userId)) {
            throw new SecurityException("You cannot modify another user's cart!");
        }

        item.setQuantity(req.getQuantity());
        cartItemRepository.save(item);
        return mapToResponse(item.getShoppingCart());
    }

    @Transactional
    public void removeCartItem(Long userId, Long cartItemId) {
        CartItem item = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Cart item not found: " + cartItemId));

        if (!item.getShoppingCart().getUser().getId().equals(userId)) {
            throw new SecurityException("You cannot delete another user's cart item!");
        }

        cartItemRepository.delete(item);
    }

    @Transactional
    public void clear(ShoppingCart cart) {
        cartItemRepository.deleteAll(cart.getCartItems());
        cart.getCartItems().clear();
    }

    private ShoppingCartResponseDto mapToResponse(ShoppingCart cart) {
        ShoppingCartResponseDto r = new ShoppingCartResponseDto();
        r.setId(cart.getId());
        r.setUserId(cart.getUser().getId());
        r.setCartItems(cart.getCartItems().stream()
                .map(this::mapToItemResponse)
                .collect(Collectors.toSet()));
        return r;
    }

    private CartItemResponseDto mapToItemResponse(CartItem item) {
        CartItemResponseDto r = new CartItemResponseDto();
        r.setId(item.getId());
        r.setBookId(item.getBook().getId());
        r.setBookTitle(item.getBook().getTitle());
        r.setQuantity(item.getQuantity());
        return r;
    }
}
