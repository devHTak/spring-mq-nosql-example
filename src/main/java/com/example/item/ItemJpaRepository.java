package com.example.item;

import org.springframework.data.repository.CrudRepository;

public interface ItemJpaRepository extends CrudRepository<Item, Long> {
}
