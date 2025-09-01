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
import com.main.orderservice.payloads.order.OrderRequestDTO;
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
    public OrderDTO placeOrder(OrderRequestDTO orderRequestDTO) {
        UserResponse user = userHttpInterfaceProvider.getUserById(orderRequestDTO.getUserId());
        Cart cart = cartRepository.findCartByUserId(user.getUserId());
        if (cart == null) {
            throw new ResourceNotFoundException("Cart", "email", user.getEmail());
        }

        AddressResponse address = userHttpInterfaceProvider.getAddressById(orderRequestDTO.getAddressId());

        Order order = new Order();
        order.setEmail(user.getEmail());
        order.setOrderDate(LocalDate.now());
        order.setTotalAmount(cart.getTotalPrice());
        order.setOrderStatus("Order Accepted !");
        order.setAddressId(address.getAddressId());

        Order savedOrder = orderRepository.save(order);

        PaymentDTO paymentRequest = PaymentDTO.builder()
                .paymentMethod(orderRequestDTO.getPaymentMethod())
                .paymentGatewayId(orderRequestDTO.getPaymentGatewayId())
                .paymentGatewayName(orderRequestDTO.getPaymentGatewayName())
                .paymentGatewayStatus(orderRequestDTO.getPaymentGatewayStatus())
                .paymentGatewayResponseMessage(orderRequestDTO.getPaymentGatewayResponseMessage())
                .build();
        paymentRequest = paymentHttpInterfaceProvider.savePayment(order.getOrderId(), paymentRequest);

        savedOrder.setPaymentId(paymentRequest.getPaymentId());
        savedOrder = orderRepository.save(order);

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

        List<CartItem> itemsToProcess = new ArrayList<>(cart.getCartItems());
        for (CartItem item : itemsToProcess) {
            int quantity = item.getQuantity();
            ProductResponse product = productHttpInterfaceProvider.getProductById(item.getProductId());
            ProductUpdateRequest productRequest = ProductUpdateRequest.builder()
                    .productId(product.getProductId())
                    .quantity(product.getQuantity() - quantity)
                    .build();
            productHttpInterfaceProvider.updateProduct(productRequest);
        }
        cart.setTotalPrice(0.0);
        cart.getCartItems().clear();
        cartRepository.save(cart);

        List<OrderItemDTO> orderItemDTOS =
                orderItems.stream()
                        .map(this::convertToDTO)
                        .toList();
        OrderDTO orderDTO = convertToOrderDTO(order, orderItemDTOS);
        orderDTO.setAddressId(orderDTO.getAddressId());

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
                .productId(orderItem.getProductId())
                .quantity(orderItem.getQuantity())
                .discount(orderItem.getDiscount())
                .orderedProductPrice(orderItem.getOrderedProductPrice())
                .build();
    }

}
