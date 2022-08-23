package com.example.pageCount;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class DailyPageCountServiceTest {

    @Autowired
    private DailyPageCountService dailyPageCountService;

    @Autowired
    private RedisTemplate redisTemplate;

    private final List<String> eventIds = Arrays.asList("123", "456", "789");

    @BeforeEach
    void beforeEach() {
        ValueOperations valueOperations = redisTemplate.opsForValue();
        String today = dailyPageCountService.getToday();
        eventIds.forEach(eventId -> valueOperations.set("event:click:daily:" + today + ":" + eventId, 0));

        valueOperations.set("event:click:daily:total:", 0);
    }

    @Test
    @DisplayName("금일 조회 확인")
    void todayClickEventCountTest() {
        Long firstVisit = dailyPageCountService.addVisit(eventIds.get(0));
        assertTrue(firstVisit > 0);

        Long secondVisit = dailyPageCountService.addVisit(eventIds.get(1));
        assertTrue(secondVisit > 0);

        Long thirdVisit = dailyPageCountService.addVisit(eventIds.get(2));
        assertTrue(thirdVisit > 0);
    }

    @Test
    @DisplayName("요일에 대하여 확인")
    void todayClieckEventTotalCountTest() {
        Long totalEvent = eventIds.stream()
                .map(eventId -> dailyPageCountService.addVisit(eventId))
                .reduce(0L, (a, b) -> a + b);

        assertEquals(totalEvent, dailyPageCountService.getVisitTotalCount(dailyPageCountService.getToday()));
    }

}