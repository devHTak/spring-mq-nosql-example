package com.example.cart.repository;

import com.example.cart.entity.Item;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ItemRepository extends CrudRepository<Item, String> {
    @Override
    List<Item> findAll();
}
