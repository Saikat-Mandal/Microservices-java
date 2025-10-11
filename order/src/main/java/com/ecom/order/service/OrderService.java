package com.ecom.order.service;

import com.ecom.order.dto.OrderItemDTO;
import com.ecom.order.dto.OrderResponse;
import com.ecom.order.model.OrderStatus;
import com.ecom.order.model.CartItem;
import com.ecom.order.model.Order;
import com.ecom.order.model.OrderItem;
import com.ecom.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderService {

//    private final UserRepository userRepository;
    private final CartService cartService;
    private final OrderRepository orderRepository;

    public Optional<OrderResponse> createOrder(String userId) {
        List<CartItem> cartItems = cartService.getCartForUser(userId);
        if(cartItems.isEmpty()) {
            throw new RuntimeException("No cart found for user " + userId);
        }
//        User user = userRepository.findById(Long.valueOf(userId))
//                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        BigDecimal totalPrice = cartItems.stream()
                .map(CartItem::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Order order = new Order();
        order.setUserId(userId);
        order.setStatus(OrderStatus.CONFIRMED);
        order.setTotalAmount(totalPrice);
        List<OrderItem> orderItems = cartItems.stream()
                .map(item->new OrderItem(
                        null,
                        item.getProductId(),
                        item.getQuantity(),
                        item.getPrice(),
                        order
                )).toList();
        order.setItems(orderItems);
        Order savedOrder = orderRepository.save(order);

//        clear the cart
        cartService.clearCart(userId);

        return Optional.of(mapToOrderResponse(savedOrder));
    }

    private OrderResponse mapToOrderResponse(Order order) {
        return new OrderResponse(
                order.getId(),
                order.getTotalAmount(),
                order.getStatus(),
                order.getItems().stream()
                        .map(orderItem -> new OrderItemDTO(
                                orderItem.getId(),
                                orderItem.getProductId(),
                                orderItem.getQuantity(),
                                orderItem.getPrice()
                        ))
                        .toList(),
                order.getCreatedAt()
        );
    }
}
