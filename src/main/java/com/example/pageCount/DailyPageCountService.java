package com.example.pageCount;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.*;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
public class DailyPageCountService {

    private final RedisTemplate redisTemplate;
    private static final String KEY_EVENT_CLICK_DAILY_TOTAL = "event:click:daily:total:";
    private static final String KEY_EVENT_CLICK_DAILY = "event:click:daily:";

    @Autowired
    public DailyPageCountService(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public Long addVisit(String eventId) {
        ValueOperations valueOperations = redisTemplate.opsForValue();
        String today = getToday();
        valueOperations.increment(KEY_EVENT_CLICK_DAILY_TOTAL + today);

        return valueOperations.increment(KEY_EVENT_CLICK_DAILY + today + ":" + eventId);
    }

    public String getVisitTotalCount(String date) {
        ValueOperations valueOperations = redisTemplate.opsForValue();
        return (String) valueOperations.get(KEY_EVENT_CLICK_DAILY_TOTAL + date);
    }

    public List<String> getVisitCountByDates(String eventId, List<String> dates) {
        ValueOperations valueOperations = redisTemplate.opsForValue();
        return dates.stream()
                .map(date -> (String) valueOperations.get(KEY_EVENT_CLICK_DAILY + date + ":" + eventId))
                .collect(toList());
    }

    protected String getToday() {
        return LocalDate.now().format(DateTimeFormatter.BASIC_ISO_DATE);
    }

}
