package com.example.pageCount;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;

@Service
public class HashPageCountService {
    private final RedisTemplate redisTemplate;
    private static final String KEY_EVENT_CLICK_DAILY_TOTAL = "event:click:total:hash";
    private static final String KEY_EVENT_CLICK_DAILY = "event:click:hash:";

    @Autowired
    public HashPageCountService(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public Integer addVisit(String eventId) {
        HashOperations hashOperations = redisTemplate.opsForHash();
        String today = getToday();

        hashOperations.put(KEY_EVENT_CLICK_DAILY_TOTAL, today,
                (Integer) hashOperations.get(KEY_EVENT_CLICK_DAILY_TOTAL, today) + 1);

        hashOperations.put(KEY_EVENT_CLICK_DAILY + eventId, today,
                (Integer) hashOperations.get(KEY_EVENT_CLICK_DAILY + eventId, today) + 1);
        return (Integer) hashOperations.get(KEY_EVENT_CLICK_DAILY + eventId, today);
    }

    public Map<String, String> getVisitCountByDailyTotal() {
        HashOperations hashOperations = redisTemplate.opsForHash();
        return hashOperations.entries(KEY_EVENT_CLICK_DAILY_TOTAL);
    }

    public Map<String, String> getVisitCountByDaily(String eventId, String date) {
        HashOperations hashOperations = redisTemplate.opsForHash();
        return hashOperations.entries(KEY_EVENT_CLICK_DAILY + eventId);
    }

    protected String getToday() {
        return LocalDate.now().format(DateTimeFormatter.BASIC_ISO_DATE);
    }
}
