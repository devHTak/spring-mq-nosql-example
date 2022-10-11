package com.example.redis.item;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
class ItemControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ItemJpaRepository itemRepository;

    @Test
    @DisplayName("Item 저장")
    void saveItemTest() throws Exception {
        // given
        ItemDto itemDto = new ItemDto();
        itemDto.setName("Test");
        itemDto.setPrice(1000);

        // when, then
        mockMvc.perform(post("/items")
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .content(objectMapper.writeValueAsString(itemDto)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Test"))
                .andExpect(jsonPath("$.price").value(1000))
                .andExpect(jsonPath("$.id").exists());
    }

    @Test
    @DisplayName("cache 에서 상품 가져오기")
    void getItemByCache() throws Exception {
        // given
        Item item = new Item();
        item.setName("TEST1");
        item.setPrice(1000);
        Item saveItem = itemRepository.save(item);

        System.out.println(saveItem.getId());
        //redisTemplate.opsForHash().put("item:" + saveItem.getId(), saveItem.getId(), saveItem);

        // when, then
        mockMvc.perform(get("/items/{itemId}", saveItem.getId()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(saveItem.getId()))
                .andExpect(jsonPath("$.name").value(saveItem.getName()))
                .andExpect(jsonPath("$.price").value(saveItem.getPrice()));

    }

}