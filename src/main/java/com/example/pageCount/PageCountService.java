package com.example.pageCount;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

@Service
public class PageCountService {

    /**
     * opsForValue	Strings를 쉽게 Serialize / Deserialize 해주는 interface
     * opsForList	List를 쉽게 Serialize / Deserialize 해주는 interface
     * opsForSet	Set을 쉽게 Serialize / Deserialize 해주는 interface
     * opsForZSet	ZSet을 쉽게 Serialize / Deserialize 해주는 interface
     * opsForHash	Hash를 쉽게 Serialize / Deserialize 해주는 interface
     */
    private final RedisTemplate redisTemplate;
    private static final String KEY_EVENT_CLICK = "event:click:";
    private static final String KEY_EVENT_CLICK_TOTAL = "event:click:total";

    @Autowired
    public PageCountService(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    /**
     * 요청된 이벤트 페이지의 방문 횟수와 전체 이벤트 페이지의 방문 횟수 증가
     * @param eventId 이벤트 아이디
     * @return 요청된 이벤트 페이지의 총 방문 횟수
     */
    public Long addVisit(String eventId) {
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        valueOperations.increment(KEY_EVENT_CLICK_TOTAL);
        return valueOperations.increment(KEY_EVENT_CLICK + eventId);
    }

    public String getVisitTotalCount() {
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        return valueOperations.get(KEY_EVENT_CLICK_TOTAL);
    }
}
