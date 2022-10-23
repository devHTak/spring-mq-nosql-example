package com.example.redis.item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class ItemController {

    private final ItemService itemService;

    @Autowired
    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @GetMapping("/items/{itemId}")
    @Cacheable(value = "Items", key="#itemId")
    public ResponseEntity<Item> getItemById(@PathVariable Long itemId) {
        Item item = itemService.getById(itemId);

        return ResponseEntity.ok(item);
    }

    @PostMapping("/items")
    public ResponseEntity<Item> save(@RequestBody ItemDto itemDto) {
        Item item = itemService.save(itemDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(item);
    }

    @PutMapping("/items/{itemId}")
    @CachePut(value = "Items", key="#itemId")
    public ResponseEntity<Item> updateItemById(@PathVariable Long itemId, @RequestBody ItemDto itemDto) {
        Item item = itemService.update(itemId, itemDto);

        return ResponseEntity.ok(item);
    }
}
