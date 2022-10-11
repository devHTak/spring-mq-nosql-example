package com.example.redis.cart.controller;

import com.example.redis.cart.dto.CartDto;
import com.example.redis.cart.entity.Cart;
import com.example.redis.cart.entity.Item;
import com.example.redis.cart.entity.Member;
import com.example.redis.cart.repository.CartRepository;
import com.example.redis.cart.repository.ItemRepository;
import com.example.redis.cart.repository.MemberRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = WebEnvironment.MOCK)
@AutoConfigureMockMvc
class CartControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void beforeEach() {
        List<Member> members = Arrays.asList(
                new Member("TEST1"), new Member("TEST2"), new Member("TEST3")
        );
        memberRepository.saveAll(members);

        List<Item> items = Arrays.asList(
                new Item("TEST1", 1000), new Item("TEST2", 2000), new Item("TEST3", 3000)
        );
        itemRepository.saveAll(items);
    }

    @Test
    @DisplayName("회원ID에 매핑되는 카트 목록 조회")
    void getCartListByMemberId() throws Exception {
        // given
        Member member = memberRepository.findAll().get(0);
        List<Item> items = itemRepository.findAll();
        List<Cart> carts = items.stream().map(item -> {
            Cart cart = new Cart(member, item, 10, 10 * item.getPrice());
            return cartRepository.save(cart);
        }).collect(Collectors.toList());

        // when, then
        mockMvc.perform(get("/carts/members/{memberId}", member.getId()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[?(@.member.name == '%s')]", member.getName()).exists());
    }

    @Test
    @DisplayName("카트 조회")
    void getCartByIdSuccessTest() throws Exception {
        // given
        Member member = memberRepository.findAll().get(0);
        Item item = itemRepository.findAll().get(0);
        Cart cart = new Cart(member, item, 10, 10 * item.getPrice());
        Cart saveCart = cartRepository.save(cart);

        // when, then
        mockMvc.perform(get("/carts/{cartId}", saveCart.getId()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists());
    }

    @Test
    @DisplayName("카트 저장")
    void saveCartSuccessTest() throws Exception {
        //given
        String memberId = memberRepository.findAll().get(0).getId();
        String itemId = itemRepository.findAll().get(0).getId();
        CartDto cartDto = new CartDto(10, memberId, itemId);

        // when, then
        mockMvc.perform(post("/carts")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(cartDto)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists());
    }

    @Test
    @DisplayName("카트 삭제")
    void deleteCartSuccessTest() throws Exception {
        // given
        Member member = memberRepository.findAll().get(0);
        Item item = itemRepository.findAll().get(0);
        int count = 10;
        Cart cart = new Cart(member, item, count, item.getPrice() * count);
        Cart saveCart = cartRepository.save(cart);

        // when, then
        mockMvc.perform(delete("/carts/{cartId}",saveCart.getId()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(saveCart.getId()));
    }

}