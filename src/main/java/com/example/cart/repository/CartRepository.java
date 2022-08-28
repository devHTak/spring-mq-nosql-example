package com.example.cart;

import com.example.cart.entity.Cart;
import org.springframework.data.repository.CrudRepository;

public interface CartRepository extends CrudRepository<Cart, String > {
}
