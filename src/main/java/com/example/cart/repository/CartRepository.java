package com.example.cart.repository;

import com.example.cart.entity.Cart;
import org.springframework.data.domain.Example;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.QueryByExampleExecutor;

import java.util.List;

public interface CartRepository extends CrudRepository<Cart, String>, QueryByExampleExecutor<Cart> {

    @Override
    List<Cart> findAll();

}
