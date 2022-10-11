package com.example.redis.log.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class WasLogServiceTest {

    @Autowired
    private WasLogService wasLogService;
    @Autowired
    private WasLogListService wasLogListService;
    @Autowired
    private RedisTemplate<String, String> redisTemplate;
    private final String KEY_WAS_LOG = "was:log";

    @Test
    @DisplayName("WAS LOG 데이터를 String Type으로 저장 후 읽고 파일 저장")
    void stringTypeWasLogReadAndWrite() {
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        // 로그 저장 확인
        for(int i = 0; i < 5; i++) {
            wasLogService.log("TEST" + i);
            assertNotNull(valueOperations.get(KEY_WAS_LOG));

            String fileName = wasLogService.writeLog();
            assertNotNull(fileName);
        }
    }

    @Test
    @DisplayName("WAS LOG 데이터를 List Type으로 저장 후 읽고 파일 저장")
    void listTypeWasLogReadAndWrite() {
        ListOperations<String, String> listOperations = redisTemplate.opsForList();
        int expected = 5;
        // 로그 저장 확인
        for(int i = 0; i < expected; i++) {
            wasLogListService.log("TEST" + i);
        }

        long actual = wasLogListService.writeLog();
        assertEquals(expected, actual);
    }

}