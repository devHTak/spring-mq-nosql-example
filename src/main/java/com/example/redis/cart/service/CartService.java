package com.example.redis.cart.service;

import com.example.redis.cart.dto.CartDto;
import com.example.redis.cart.entity.Cart;
import com.example.redis.cart.entity.Item;
import com.example.redis.cart.entity.Member;
import com.example.redis.cart.repository.CartRepository;
import com.example.redis.cart.repository.ItemRepository;
import com.example.redis.cart.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class CartService {
    private final CartRepository cartRepository;
    private final MemberRepository memberRepository;
    private final ItemRepository itemRepository;

    @Autowired
    public CartService(CartRepository cartRepository, MemberRepository memberRepository, ItemRepository itemRepository) {
        this.cartRepository = cartRepository;
        this.memberRepository = memberRepository;
        this.itemRepository = itemRepository;
    }

    public List<Cart> getCartList(String memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(IllegalArgumentException::new);

        Cart cart = new Cart();
        cart.setMember(member);
        Example<Cart> cartExample = Example.of(cart);
        List<Cart> result = new ArrayList<>();
        cartRepository.findAll(cartExample).forEach(result::add);

        return result;
    }

    public Cart getCartById(String cartId) {
        return cartRepository.findById(cartId)
                .orElseThrow(IllegalArgumentException::new);
    }

    public Cart saveCart(CartDto cartDto) {
        Member member = memberRepository.findById(cartDto.getMemberId())
                .orElseThrow(IllegalArgumentException::new);

        Item item = itemRepository.findById(cartDto.getItemId())
                .orElseThrow(IllegalArgumentException::new);

        Cart cart = new Cart(member, item, cartDto.getCount(), item.getPrice() * cartDto.getCount());
        return cartRepository.save(cart);
    }

    public Cart deleteCart(String cartId) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(IllegalArgumentException::new);
        cartRepository.delete(cart);

        return cart;
    }
}
