package com.ecom.order.controller;

import com.ecom.order.dto.CartItemRequest;
import com.ecom.order.model.CartItem;
import com.ecom.order.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/cart")
public class CartItemController {

    private final CartService cartService;

    @PostMapping
    public ResponseEntity<?> addToCart(@RequestHeader("X-User-ID") String userId,
                                          @RequestBody CartItemRequest request) {
        cartService.addToCart(userId, request);
        return new ResponseEntity<>( "Added to cart successfully",HttpStatus.OK);
    }

    @DeleteMapping("/items/{productId}")
    public ResponseEntity<?> removeFromCart(@RequestHeader("X-User-ID") String userId ,@PathVariable String productId) {
        cartService.deleteFromCart(userId , productId);
        return new ResponseEntity<>( "Removed from cart successfully",HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<CartItem>> getCartForUser(@RequestHeader("X-User-ID") String userId) {
        return new ResponseEntity<>(cartService.getCartForUser(userId), HttpStatus.OK);
    }
}
