package com.main.orderservice.serviceImpl;

import com.main.orderservice.client.paymentservice.PaymentHttpInterfaceProvider;
import com.main.orderservice.client.productservice.ProductHttpInterfaceProvider;
import com.main.orderservice.client.userservice.UserHttpInterfaceProvider;
import com.main.orderservice.exceptions.custom.APIException;
import com.main.orderservice.exceptions.custom.ResourceNotFoundException;
import com.main.orderservice.model.Cart;
import com.main.orderservice.model.CartItem;
import com.main.orderservice.model.Order;
import com.main.orderservice.model.OrderItem;
import com.main.orderservice.payloads.order.OrderDTO;
import com.main.orderservice.payloads.order.OrderItemDTO;
import com.main.orderservice.payloads.payment.PaymentDTO;
import com.main.orderservice.payloads.product.ProductResponse;
import com.main.orderservice.payloads.product.ProductUpdateRequest;
import com.main.orderservice.payloads.user.AddressResponse;
import com.main.orderservice.payloads.user.UserResponse;
import com.main.orderservice.repository.CartRepository;
import com.main.orderservice.repository.OrderItemRepository;
import com.main.orderservice.repository.OrderRepository;
import com.main.orderservice.service.CartService;
import com.main.orderservice.service.OrderService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final CartRepository cartRepository;
    private final OrderItemRepository orderItemRepository;
    private final OrderRepository orderRepository;
    private final CartService cartService;
    private final UserHttpInterfaceProvider userHttpInterfaceProvider;
    private final ProductHttpInterfaceProvider productHttpInterfaceProvider;
    private final PaymentHttpInterfaceProvider paymentHttpInterfaceProvider;

    @Override
    @Transactional
    public OrderDTO placeOrder(String emailId,
                               Long addressId,
                               String paymentMethod,
                               String paymentGatewayName,
                               String paymentGatewayId,
                               String paymentGatewayStatus,
                               String paymentGatewayResponseMessage) {

        UserResponse user = userHttpInterfaceProvider.getUserByEmailId(emailId);
        Cart cart = cartRepository.findCartByUserId(user.getUserId());
        if (cart == null) {
            throw new ResourceNotFoundException("Cart", "email", emailId);
        }

        AddressResponse address = userHttpInterfaceProvider.getAddressById(addressId);

        Order order = new Order();
        order.setEmail(emailId);
        order.setOrderDate(LocalDate.now());
        order.setTotalAmount(cart.getTotalPrice());
        order.setOrderStatus("Order Accepted !");
        order.setAddressId(address.getAddressId());

        PaymentDTO paymentRequest = PaymentDTO.builder()
                .paymentMethod(paymentMethod)
                .paymentGatewayId(paymentGatewayId)
                .paymentGatewayName(paymentGatewayName)
                .paymentGatewayStatus(paymentGatewayStatus)
                .paymentGatewayResponseMessage(paymentGatewayResponseMessage)
                .build();

        paymentRequest = paymentHttpInterfaceProvider.savePayment(order.getOrderId(), paymentRequest);
        order.setPaymentId(paymentRequest.getPaymentId());

        Order savedOrder = orderRepository.save(order);

        List<CartItem> cartItems = cart.getCartItems();
        if (cartItems.isEmpty()) {
            throw new APIException("Cart is empty");
        }

        List<OrderItem> orderItems = new ArrayList<>();
        for (CartItem cartItem : cartItems) {
            OrderItem orderItem = new OrderItem();
            orderItem.setProductId(cartItem.getProductId());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setDiscount(cartItem.getDiscount());
            orderItem.setOrderedProductPrice(cartItem.getPrice());
            orderItem.setOrder(savedOrder);
            orderItems.add(orderItem);
        }

        orderItems = orderItemRepository.saveAll(orderItems);


        Iterator<CartItem> iterator = cart.getCartItems().iterator();
        while (iterator.hasNext()) {
            CartItem item = iterator.next();
            int quantity = item.getQuantity();
            ProductResponse product = productHttpInterfaceProvider.getProductById(item.getProductId());
            product.setQuantity(product.getQuantity() - quantity);

            ProductUpdateRequest productRequest = ProductUpdateRequest.builder()
                    .productId(product.getProductId())
                    .quantity(quantity)
                    .build();
            productHttpInterfaceProvider.updateProduct(productRequest);

            iterator.remove();

            cartService.deleteProductFromCart(cart.getCartId(), item.getProductId());
        }

        List<OrderItemDTO> orderItemDTOS =
                orderItems.stream()
                        .map(this::convertToDTO)
                                .toList();
        OrderDTO orderDTO = convertToOrderDTO(order, orderItemDTOS);
        orderDTO.setAddressId(addressId);

        return orderDTO;
    }

    private OrderDTO convertToOrderDTO(Order order, List<OrderItemDTO> orderItemsDto) {
        return OrderDTO.builder()
                .orderId(order.getOrderId())
                .email(order.getEmail())
                .orderItems(orderItemsDto)
                .orderDate(order.getOrderDate())
                .paymentId(order.getPaymentId())
                .totalAmount(order.getTotalAmount())
                .orderStatus(order.getOrderStatus())
                .addressId(order.getAddressId())
                .build();
    }

    private OrderItemDTO convertToDTO(OrderItem orderItem) {
        return OrderItemDTO.builder()
                .orderItemId(orderItem.getOrderItemId())
                .product(productHttpInterfaceProvider.getProductById(orderItem.getProductId()))
                .quantity(orderItem.getQuantity())
                .discount(orderItem.getDiscount())
                .orderedProductPrice(orderItem.getOrderedProductPrice())
                .build();
    }

}
