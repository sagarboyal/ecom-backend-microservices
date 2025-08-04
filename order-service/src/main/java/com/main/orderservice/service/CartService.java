package com.main.orderservice.service;

import com.main.orderservice.payloads.cart.CartDTO;
import com.main.orderservice.payloads.cart.CartRequest;

import java.util.List;

public interface CartService {
    CartDTO addProductToCart(CartRequest request);
    List<CartDTO> getAllCarts();
    Long findUserCartId(Long userId);
    CartDTO getUserCart(Long userId, Long cartId);
    CartDTO updateProductQuantityInCart(CartRequest request);
    String deleteProductFromCart(Long cartId, String productId);
}
