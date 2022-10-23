package com.example.redis.viewCount;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Service
public class ViewCountService {

    private final RedisTemplate<String, String> redisTemplate;
    private final static String KEY_PAGE_VIEW = "page:view:";
    private final static String KEY_UNIQUE_VISITOR = "unique:visitors:";

    @Autowired
    public ViewCountService(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void visit(int userNo) {
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        valueOperations.increment(KEY_PAGE_VIEW + getToday());
        valueOperations.setBit(KEY_UNIQUE_VISITOR + getToday(), userNo, true);
    }

    public int getPageViewCount(String date) {
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        return Integer.parseInt(valueOperations.get(KEY_PAGE_VIEW + date));
    }

    public long getUniquePageViewCount(String date) {
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();

        return Long.bitCount(Integer.parseInt(valueOperations.get(KEY_UNIQUE_VISITOR + date)));
    }

    private String getToday() {
        return LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
    }
}
