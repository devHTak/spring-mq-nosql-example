package com.example.redis.item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ItemService {

    private final ItemJpaRepository itemRepository;

    @Autowired
    public ItemService(ItemJpaRepository itemRepository) {
        this.itemRepository = itemRepository;
    }


    public Item getById(Long itemId) {
        return itemRepository.findById(itemId).orElseThrow(IllegalArgumentException::new);
    }

    public Item save(ItemDto itemDto) {
        Item item = new Item();
        item.setName(itemDto.getName());
        item.setPrice(itemDto.getPrice());

        return itemRepository.save(item);
    }

    public Item update(Long itemId, ItemDto itemDto) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(IllegalArgumentException::new);

        item.setName(itemDto.getName());
        item.setPrice(itemDto.getPrice());

        return item;
    }
}
