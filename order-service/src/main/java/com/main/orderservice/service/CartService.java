package com.main.orderservice.service;

import com.main.orderservice.payloads.cart.CartDTO;

import java.util.List;

public interface CartService {
    CartDTO addProductToCart(Long userId, String productId, Integer quantity);

    List<CartDTO> getAllCarts();

    Long findUserCartId(Long userId);

    CartDTO getUserCart(Long userId, Long cartId);

    CartDTO updateProductQuantityInCart(Long userId, String productId, Integer quantity);

    String deleteProductFromCart(Long cartId, String productId);

    void updateProductInCarts(Long cartId, String productId);
}
