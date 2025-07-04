package com.main.orderservice.serviceImpl;

import com.main.orderservice.client.productservice.ProductHttpInterfaceProvider;
import com.main.orderservice.client.userservice.UserHttpInterfaceProvider;
import com.main.orderservice.exceptions.custom.APIException;
import com.main.orderservice.exceptions.custom.ResourceNotFoundException;
import com.main.orderservice.model.Cart;
import com.main.orderservice.model.CartItem;
import com.main.orderservice.payloads.cart.CartDTO;
import com.main.orderservice.payloads.product.ProductResponse;
import com.main.orderservice.repository.CartItemRepository;
import com.main.orderservice.repository.CartRepository;
import com.main.orderservice.service.CartService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductHttpInterfaceProvider  productHttpInterfaceProvider;
    private final UserHttpInterfaceProvider userHttpInterfaceProvider;

    @Override
    public CartDTO addProductToCart(Long userId, String productId, Integer quantity) {
        Cart cart = createCart(userId);

        ProductResponse product = productHttpInterfaceProvider.getProductById(productId);
        CartItem cartItem = cartItemRepository.findCartItemByProductIdAndCartId(cart.getCartId(), productId);

        if (cartItem != null)
            throw new APIException("Product "+product.getProductName()+" has already been added to the cart");

        if (product.getQuantity() == 0)
            throw new APIException("Product "+product.getProductName()+" is out of stock");

        if (product.getQuantity() < quantity)
            throw new APIException("Please, make an order of the Product "+product.getProductName()+" less than or equal to the quantity of "+quantity+".");

        CartItem newCartItem = new CartItem();
        newCartItem.setCart(cart);
        newCartItem.setProductId(product.getProductId());
        newCartItem.setQuantity(quantity);
        newCartItem.setDiscount(product.getDiscount());
        newCartItem.setPrice(product.getSpecialPrice());

        cartItemRepository.save(newCartItem);

        product.setQuantity(product.getQuantity());

        double totalPrice = cart.getTotalPrice() + (product.getSpecialPrice() * quantity);
        cart.setTotalPrice(totalPrice);
        cart.getCartItems().add(newCartItem);
        cart = cartRepository.save(cart);

        List<CartItem> cartItems = cart.getCartItems();

        Stream<ProductResponse> productStream = cartItems.stream().map(item -> {
            ProductResponse map = productHttpInterfaceProvider.getProductById(item.getProductId());
            map.setQuantity(item.getQuantity());
            return map;
        });

        return convertToCartDTO(cart, productStream);
    }

    @Override
    public List<CartDTO> getAllCarts() {
        List<Cart> carts = cartRepository.findAll();

        if (carts.isEmpty()) throw new APIException("No carts found");

        return carts.stream().map(cart -> {
            Stream<ProductResponse> productStream = cart.getCartItems().stream().map(cartItem ->{
               ProductResponse productDTO = productHttpInterfaceProvider.getProductById(cartItem.getProductId());
                        productDTO.setQuantity(cartItem.getQuantity());
                        return productDTO;
                   });
            return convertToCartDTO(cart, productStream);
        }).toList();
    }

    @Override
    public Long findUserCartId(Long userId) {
        Cart cart = cartRepository.findCartByUserId(userId);
        if(cart == null) cart = createCart(userId);
        return cart.getCartId();
    }

    @Override
    public CartDTO getUserCart(Long userId, Long cartId) {
        Cart cart = cartRepository.findCartByUserIdAndCartId(userId, cartId);
        if (cart == null) throw new ResourceNotFoundException("cart", "id", cartId);

        cart.getCartItems().forEach(cartItem -> {
            ProductResponse product =  productHttpInterfaceProvider.getProductById(cartItem.getProductId());
            cartItem.setQuantity(product.getQuantity());
        });

        Stream<ProductResponse> productDTOList = cart.getCartItems().stream()
                .map(item -> productHttpInterfaceProvider.getProductById(item.getProductId()));

        return convertToCartDTO(cart, productDTOList);
    }

    @Transactional
    @Override
    public CartDTO updateProductQuantityInCart(Long userId, String productId, Integer quantity) {
        Cart userCart = cartRepository.findCartByUserId(userId);
        Long cartId  = userCart.getCartId();

        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart", "cartId", cartId));

        ProductResponse product = productHttpInterfaceProvider.getProductById(productId);

        if (product.getQuantity() == 0) {
            throw new APIException(product.getProductName() + " is not available");
        }

        if (product.getQuantity() < quantity) {
            throw new APIException("Please, make an order of the " + product.getProductName()
                    + " less than or equal to the quantity " + product.getQuantity() + ".");
        }

        CartItem cartItem = cartItemRepository.findCartItemByProductIdAndCartId(cartId, productId);

        if (cartItem == null) {
            throw new APIException("Product " + product.getProductName() + " not available in the cart!!!");
        }

        int newQuantity = cartItem.getQuantity() + quantity;
        if (newQuantity == 0) {
            deleteProductFromCart(cartId, productId);
            throw new APIException("Product " + product.getProductName() + " is removed from the cart!!!");
        }

        cartItem.setPrice(product.getSpecialPrice());
        cartItem.setQuantity(cartItem.getQuantity() + quantity);
        cartItem.setDiscount(product.getDiscount());

        cart.setTotalPrice(cart.getTotalPrice() + (cartItem.getPrice() * quantity));
        cartRepository.save(cart);

        CartItem updatedItem = cartItemRepository.save(cartItem);
        if(updatedItem.getQuantity() == 0){
            cartItemRepository.deleteById(updatedItem.getCartItemId());
        }

        Stream<ProductResponse> productStream = cart.getCartItems().stream().map(item -> {
            ProductResponse prd = productHttpInterfaceProvider.getProductById(item.getProductId());
            prd.setQuantity(item.getQuantity());
            return prd;
        });
        return convertToCartDTO(cart, productStream);
    }

    @Transactional
    @Override
    public String deleteProductFromCart(Long cartId, String productId) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart", "cartId", cartId));
        ProductResponse product = productHttpInterfaceProvider.getProductById(productId);
        CartItem cartItem = cartItemRepository.findCartItemByProductIdAndCartId(cartId, productId);

        if (cartItem == null) {
            throw new ResourceNotFoundException("Product", "productId", productId);
        }
        double price = cart.getTotalPrice() - (cartItem.getPrice() * cartItem.getQuantity());
        cart.setTotalPrice(price < 0 ? 0 : price);

        cartItemRepository.deleteCartItemByProductIdAndCartId(cartId, productId);

        return "Product " + product.getProductName() + " removed from the cart!";
    }


    @Override
    public void updateProductInCarts(Long cartId, String productId) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart", "cartId", cartId));

        ProductResponse product = productHttpInterfaceProvider.getProductById(productId);
        CartItem cartItem = cartItemRepository.findCartItemByProductIdAndCartId(cartId, productId);

        if (cartItem == null) {
            throw new APIException("Product " + product.getProductName() + " not available in the cart!!!");
        }

        double cartPrice = cart.getTotalPrice()
                - (cartItem.getPrice() * cartItem.getQuantity());

        cartItem.setPrice(product.getSpecialPrice());

        cart.setTotalPrice(cartPrice
                + (cartItem.getPrice() * cartItem.getQuantity()));

        cartItemRepository.save(cartItem);
    }

    private Cart createCart(Long userId) {
        Cart userCart = cartRepository.findCartByUserId(userId);
        if (userCart != null)
            return userCart;

        Cart newCart = new Cart();
        newCart.setTotalPrice(0.00);
        newCart.setUserId(userId);
        return cartRepository.save(newCart);
    }

    private CartDTO convertToCartDTO(Cart cart, Stream<ProductResponse> productStream) {
        return CartDTO.builder()
                .cartId(cart.getCartId())
                .totalPrice(cart.getTotalPrice())
                .products(productStream.toList())
                .build();
    }
}
