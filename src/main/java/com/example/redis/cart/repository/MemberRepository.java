package com.example.redis.cart.repository;

import com.example.redis.cart.entity.Member;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface MemberRepository extends CrudRepository<Member, String> {
    @Override
    List<Member> findAll();
}
