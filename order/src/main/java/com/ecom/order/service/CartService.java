package com.ecom.order.service;

import com.ecom.order.dto.CartItemRequest;
import com.ecom.order.exception.OutOfStockException;
import com.ecom.order.exception.ResourceNotFoundException;
import com.ecom.order.model.CartItem;
import com.ecom.order.repository.CartItemRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CartService {

//    private final ProductRepository productRepository;
    private final CartItemRepository cartItemRepository;
//    private final UserRepository userRepository;

    public void addToCart(String userId, CartItemRequest request) {
//        Product product = productRepository.findById(request.getProductId()).orElseThrow(() -> new ResourceNotFoundException("Product not found"));

//        if (!product.getActive()) {
//            throw new ResourceNotFoundException("Product not found");
//        }

//        if (product.getStockQuantity() < request.getQuantity()) {
//            throw new OutOfStockException("Only " + product.getStockQuantity() + " items available in stock");
//        }

//        User user = userRepository.findById(Long.value(userId));
//                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        CartItem existingCartitem = cartItemRepository.findByUserIdAndProductId(userId, request.getProductId());

        if (existingCartitem != null) {
            int newQuantity = existingCartitem.getQuantity() + request.getQuantity();
//            if (newQuantity > product.getStockQuantity()) {
//                throw new OutOfStockException("Only " + product.getStockQuantity() + " items available in stock");
//            }
            existingCartitem.setQuantity(newQuantity);
            existingCartitem.setPrice(BigDecimal.valueOf(1000.00));
            cartItemRepository.save(existingCartitem);
        } else {
            CartItem cartItem = new CartItem();
            cartItem.setUserId(userId);
            cartItem.setProductId(request.getProductId());
            cartItem.setQuantity(request.getQuantity());
            cartItem.setPrice(BigDecimal.valueOf(1000.00));
//            cartItem.setPrice(product.getPrice().multiply(BigDecimal.valueOf(request.getQuantity())));
            cartItemRepository.save(cartItem);
        }
    }

    public void deleteFromCart(String userId, String productId) {
//        Product product = productRepository.findById(productId)
//                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));
//
//        if (!product.getActive()) {
//            throw new ResourceNotFoundException("Product not found");
//        }
//
//        User user = userRepository.findById(Long.valueOf(userId))
//                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        CartItem cartItem = cartItemRepository.findByUserIdAndProductId(userId, productId);
        cartItemRepository.deleteByUserIdAndProductId(userId, productId);

    }

    public List<CartItem> getCartForUser(String userId) {
//        User user = userRepository.findById(Long.valueOf(userId))
//                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return cartItemRepository.findByUserId(userId);
    }

    public void clearCart(String userId) {
//        User user =  userRepository.findById(Long.valueOf(userId)).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        cartItemRepository.deleteByUserId(userId);
    }
}
