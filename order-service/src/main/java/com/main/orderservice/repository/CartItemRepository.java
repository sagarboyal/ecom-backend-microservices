package com.main.orderservice.repository;

import com.main.orderservice.model.CartItem;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    @Query("SELECT ci FROM CartItem ci WHERE ci.cart.cartId = ?1 AND ci.productId = ?2")
    CartItem findCartItemByProductIdAndCartId(Long cartId, String productId);

    @Modifying
    @Query("DELETE FROM CartItem ci WHERE ci.cart.cartId = ?1 AND ci.productId = ?2")
    void deleteCartItemByProductIdAndCartId(Long cartId, String productId);

    @Modifying
    @Transactional
    @Query("DELETE FROM CartItem ci WHERE ci.productId = :productId")
    void deleteByProductId(@Param("productId") String productId);
}
