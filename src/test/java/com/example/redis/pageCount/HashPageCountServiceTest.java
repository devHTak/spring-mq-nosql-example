package com.example.redis.pageCount;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class HashPageCountServiceTest {
    @Autowired
    private HashPageCountService hashPageCountService;

    @Autowired
    private RedisTemplate redisTemplate;

    private static final String KEY_EVENT_CLICK_DAILY_TOTAL = "event:click:total:hash";
    private static final String KEY_EVENT_CLICK_DAILY = "event:click:hash:";
    private final List<String> eventIds = Arrays.asList("123", "456", "789");

    @BeforeEach
    void beforeEach() {
        String today = hashPageCountService.getToday();

        HashOperations hashOperations = redisTemplate.opsForHash();
        hashOperations.put(KEY_EVENT_CLICK_DAILY_TOTAL, today, 0);
        eventIds.stream().forEach(eventId -> hashOperations.put(KEY_EVENT_CLICK_DAILY + eventId, today, 0));
    }

    @Test
    @DisplayName("금일 조회 확인")
    void todayClickEventCountTest() {
        Integer firstVisit = hashPageCountService.addVisit(eventIds.get(0));
        assertTrue(firstVisit > 0);

        Integer secondVisit = hashPageCountService.addVisit(eventIds.get(1));
        assertTrue(secondVisit > 0);

        Integer thirdVisit = hashPageCountService.addVisit(eventIds.get(2));
        assertTrue(thirdVisit > 0);
    }

    @Test
    @DisplayName("요일에 대하여 확인")
    void todayClieckEventTotalCountTest() {
        Integer totalEvent = eventIds.stream()
                .map(eventId -> hashPageCountService.addVisit(eventId))
                .reduce(0, (a, b) -> a + b);

        Map<String, String> visitCountByDailyTotal = hashPageCountService.getVisitCountByDailyTotal();
        assertEquals(totalEvent, visitCountByDailyTotal.get(hashPageCountService.getToday()));
    }
}