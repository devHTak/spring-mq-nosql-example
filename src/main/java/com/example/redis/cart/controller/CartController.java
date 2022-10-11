package com.example.redis.cart.controller;

import com.example.redis.cart.dto.CartDto;
import com.example.redis.cart.entity.Cart;
import com.example.redis.cart.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class CartController {

    private final CartService cartService;

    @Autowired
    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping("/carts/members/{memberId}")
    public ResponseEntity<List<Cart>> getCartList(@PathVariable String memberId) {
        return ResponseEntity.ok(cartService.getCartList(memberId));
    }

    @GetMapping("/carts/{cartId}")
    public ResponseEntity<Cart> getCartById(@PathVariable String cartId) {
        return ResponseEntity.ok(cartService.getCartById(cartId));
    }

    @PostMapping("/carts")
    public ResponseEntity<Cart> saveCart(@RequestBody CartDto cartDto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(cartService.saveCart(cartDto));
    }

    @DeleteMapping("/carts/{cartId}")
    public ResponseEntity<Cart> deleteCart(@PathVariable String cartId) {
        return ResponseEntity.ok(cartService.deleteCart(cartId));
    }
}
