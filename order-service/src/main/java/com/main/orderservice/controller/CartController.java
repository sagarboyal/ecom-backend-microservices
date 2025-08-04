package com.main.orderservice.controller;

import com.main.orderservice.payloads.cart.CartDTO;
import com.main.orderservice.payloads.cart.CartRequest;
import com.main.orderservice.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/carts")
@RequiredArgsConstructor
public class CartController {
    private final CartService cartService;

    @GetMapping()
    public ResponseEntity<List<CartDTO>> getCartsHandler() {
        List<CartDTO> cartDTOS = cartService.getAllCarts();
        return new ResponseEntity<>(cartDTOS, HttpStatus.FOUND);
    }

    @GetMapping("/user/cart/{userId}")
    public ResponseEntity<CartDTO> getUserCartsHandler(@PathVariable Long userId) {
        Long cartId = cartService.findUserCartId(userId);
        CartDTO cartDTO = cartService.getUserCart(userId, cartId);
        return new ResponseEntity<>(cartDTO, HttpStatus.FOUND);
    }

    @PostMapping("/products")
    public ResponseEntity<CartDTO> saveProductHandler(@RequestBody CartRequest request) {
        CartDTO data = cartService.addProductToCart(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(data);
    }

    @PutMapping("/products")
    public ResponseEntity<CartDTO> updateProductHandler(@RequestBody CartRequest request) {
        CartDTO cartDTO = cartService.updateProductQuantityInCart(request);
        return ResponseEntity.status(HttpStatus.OK).body(cartDTO);
    }

    @DeleteMapping("/products/{productId}/cart/{cartId}")
    public ResponseEntity<String> deleteProductHandler(@PathVariable Long cartId,
                                                       @PathVariable String productId) {
        String status = cartService.deleteProductFromCart(cartId, productId);
        return ResponseEntity.status(HttpStatus.OK).body(status);
    }
}
