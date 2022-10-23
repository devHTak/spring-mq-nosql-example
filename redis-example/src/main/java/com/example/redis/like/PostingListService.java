package com.example.redis.like;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class PostingListService {

    private final RedisTemplate<String, String> redisTemplate;
    private static final String POSTING_KEY = "posting:like:";

    @Autowired
    public PostingListService(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public boolean like(String postingId, String userId) {
        SetOperations<String, String> setOperations = redisTemplate.opsForSet();
        Long count = setOperations.add(POSTING_KEY + postingId, userId);

        return count > 0;
    }

    public boolean unLike(String postintId, String userId) {
        SetOperations<String, String> setOperations = redisTemplate.opsForSet();
        Long count = setOperations.remove(POSTING_KEY + postintId, userId);

        return count > 0;
    }

    public boolean isLike(String postingId, String userId) {
        SetOperations<String, String> opsForSet = redisTemplate.opsForSet();
        return opsForSet.isMember(POSTING_KEY + postingId, userId);
    }

    public boolean deletePostingLike(String postingId) {
        SetOperations<String, String> setOperations = redisTemplate.opsForSet();
        return setOperations.getOperations().delete(POSTING_KEY + postingId);
    }

    public long getPostingLikeCount(String postingId) {
        SetOperations<String, String> setOperations = redisTemplate.opsForSet();
        Set<String> members = setOperations.members(POSTING_KEY + postingId);
        return members.size();
    }

    public long getPostingListLikeCount(List<String> postingIds) {
        return postingIds.stream().map(postingId -> getPostingLikeCount(postingId))
                .reduce(0L, (a, b) -> a + b);
    }

}
