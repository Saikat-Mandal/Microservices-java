package com.ecom.order.controller;

import com.ecom.order.dto.OrderResponse;
import com.ecom.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/orders")
public class OrderController {
    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<?> createOrder(@RequestHeader("X-User-ID") String userId) {
        Optional<OrderResponse> order = orderService.createOrder(userId);
        if(order.isPresent()) {
            return new ResponseEntity<>(order.get(), HttpStatus.CREATED);
        }
        return new ResponseEntity<>("No order created", HttpStatus.BAD_REQUEST);
    }
}
