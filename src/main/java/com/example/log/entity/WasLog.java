package com.example.log.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.time.LocalDateTime;

/**
 * Redis 에 저장할 자료구조인 객체를 정의합니다.
 * 일반적인 객체 선언 후 @RedisHash 를 붙이면 됩니다.
 * value : Redis 의 keyspace 값으로 사용됩니다.
 * timeToLive : 만료시간을 seconds 단위로 설정할 수 있습니다. 기본값은 만료시간이 없는 -1L 입니다.
 * @Id 어노테이션이 붙은 필드가 Redis Key 값이 되며 null 로 세팅하면 랜덤값이 설정됩니다.
 * keyspace 와 합쳐져서 레디스에 저장된 최종 키 값은 keyspace:id 가 됩니다.
 */
@RedisHash(value="was:log", timeToLive= 30)
public class WasLog {

    @Id
    private String id;
    private String fromWas;
    private String message;
    private LocalDateTime createdAt;

    public WasLog(String fromWas, String message, LocalDateTime createdAt) {
        this.fromWas = fromWas;
        this.message = message;
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "[" + createdAt.toString() + "(" + fromWas + ")] " + message;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFromWas() {
        return fromWas;
    }

    public void setFromWas(String fromWas) {
        this.fromWas = fromWas;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
