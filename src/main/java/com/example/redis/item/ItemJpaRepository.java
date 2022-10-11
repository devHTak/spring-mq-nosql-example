package com.example.redis.item;

import org.springframework.data.repository.CrudRepository;

public interface ItemJpaRepository extends CrudRepository<Item, Long> {
}
