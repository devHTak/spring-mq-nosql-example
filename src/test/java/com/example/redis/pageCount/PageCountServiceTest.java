package com.example.redis.pageCount;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class PageCountServiceTest {

    @Autowired
    private PageCountService pageCountService;

    @Autowired
    private RedisTemplate redisTemplate;

    private final List<String> eventIds = Arrays.asList("123", "456", "789");

    @BeforeEach
    void beforeEach() {
        ValueOperations valueOperations = redisTemplate.opsForValue();
        eventIds.forEach(eventId -> valueOperations.set("event:click:" + eventId, 0));

        valueOperations.set("event:click:total", 0);
    }


    @Test
    @DisplayName("visit 증가 확인")
    void addVisitCountTest() {
        Long firstVisit = pageCountService.addVisit(eventIds.get(0));
        assertTrue(firstVisit > 0);

        Long secondVisit = pageCountService.addVisit(eventIds.get(1));
        assertTrue(secondVisit > 0);

        Long thirdVisit = pageCountService.addVisit(eventIds.get(2));
        assertTrue(thirdVisit > 0);
    }

    @Test
    @DisplayName("total visit 확인")
    void totalVisitCountTest() {
        Long totalEvent = eventIds.stream()
                .map(eventId -> pageCountService.addVisit(eventId))
                .reduce(0L, (a, b) -> a + b);

        assertEquals(String.valueOf(totalEvent), pageCountService.getVisitTotalCount());
    }

}