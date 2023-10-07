package com.example.redis.redisson.service;

import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class TicketService {
    private Logger log = LoggerFactory.getLogger(TicketService.class);
    private final RedissonClient redissonClient;
    private final int EMPTY = 0;
    private final String KEY_PREFIX = "TICKET_";

    public TicketService(RedissonClient redissonClient) {
        this.redissonClient = redissonClient;
    }

    public String keyResolver(String code) {
        return KEY_PREFIX + code;
    }

    public void reservationTicketWithLock(String key) {
        final String lockName = key + ":lock";
        final RLock lock = redissonClient.getLock(lockName);
        final String threadName = Thread.currentThread().getName();

        try {
            if(!lock.tryLock(1, 3, TimeUnit.SECONDS)) {
                return;
            }

            int quantity = usableTicket(key);
            log.info("GET LOCK: ThreadName({}), Key({}), Quantity({})", threadName, key, quantity);
            if(quantity <= EMPTY) {
                throw new IllegalStateException("예약 불가능한 상태입니다.");
            }

            setUsableTicket(key, quantity - 1);
        } catch (InterruptedException e) {
            throw new IllegalStateException("락 획득 실패");
        } finally {
            if(lock != null && lock.isLocked()) {
                lock.unlock();
            }
        }

    }

    protected void setUsableTicket(String key, int quantity) {
        redissonClient.getBucket(key).set(quantity);
    }

    protected int usableTicket(String key) {
        return (int) redissonClient.getBucket(key).get();
    }


}
