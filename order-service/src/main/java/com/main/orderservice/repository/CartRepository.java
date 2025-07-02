package com.main.orderservice.repository;

import com.main.orderservice.model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {
    Cart findCartByUserId(Long userId);
    @Query("SELECT c FROM Cart c WHERE c.userId = ?1 AND c.cartId = ?2")
    Cart findCartByUserIdAndCartId(Long userId, Long cartId);
    @Query("SELECT c FROM Cart c JOIN FETCH c.cartItems ci WHERE ci.productId = ?1")
    List<Cart> findCartsByProductId(String productId);

}
