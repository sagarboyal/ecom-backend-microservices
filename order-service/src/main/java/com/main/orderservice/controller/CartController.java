package com.main.orderservice.controller;

import com.main.orderservice.payloads.cart.CartDTO;
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

    @GetMapping("/users/cart/{userId}")
    public ResponseEntity<CartDTO> getUserCartsHandler(@PathVariable Long userId) {
        Long cartId = cartService.findUserCartId(userId);
        CartDTO cartDTO = cartService.getUserCart(userId, cartId);
        return new ResponseEntity<>(cartDTO, HttpStatus.FOUND);
    }

    @PostMapping("/products/{productId}/quantity/{quantity}/user/{userId}")
    public ResponseEntity<CartDTO> saveProductHandler(@PathVariable String productId,
                                                      @PathVariable Integer quantity,
                                                      @PathVariable Long userId) {
        CartDTO data = cartService.addProductToCart(userId, productId, quantity);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(data);
    }

    @PutMapping("/products/{productId}/quantity/{operation}/user/{userId}")
    public ResponseEntity<CartDTO> updateProductHandler(@PathVariable String productId,
                                                        @PathVariable String operation,
                                                        @PathVariable Long userId) {
        CartDTO cartDTO = cartService.updateProductQuantityInCart(userId, productId,
                operation.equalsIgnoreCase("delete") ? -1 : 1);
        return ResponseEntity.status(HttpStatus.OK).body(cartDTO);
    }

    @DeleteMapping("/{cartId}/product/{productId}")
    public ResponseEntity<String> deleteProductHandler(@PathVariable Long cartId,
                                                       @PathVariable String productId) {
        String status = cartService.deleteProductFromCart(cartId, productId);
        return ResponseEntity.status(HttpStatus.OK).body(status);
    }
}
