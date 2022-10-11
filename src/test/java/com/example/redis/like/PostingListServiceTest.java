package com.example.redis.like;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class PostingListServiceTest {

    @Autowired
    private PostingListService postingListService;

    private List<String> postingIds;
    private String userId;

    @BeforeEach
    void beforeEach() {
        postingIds = new ArrayList<>();
        for(int i = 0; i < 5; i++) {
            postingIds.add(UUID.randomUUID().toString());
        }

        userId = UUID.randomUUID().toString();
    }

    @AfterEach
    void afterEach() {
        postingIds.forEach(postingId -> postingListService.deletePostingLike(postingId));
    }

    @Test
    @DisplayName("좋아요 기능 테스트")
    void postingLikeTest() {
        String postingId = postingIds.get(0);

        boolean like = postingListService.like(postingId, userId);
        assertTrue(like);

        assertTrue(postingListService.isLike(postingId, userId));

        long postingLikeCount = postingListService.getPostingLikeCount(postingId);
        assertEquals(1, postingLikeCount);
    }

    @Test
    @DisplayName("좋아요 취소 기능 테스트")
    void postingUnLikeTest() {
        String postingId = postingIds.get(0);

        boolean like = postingListService.like(postingId, userId);
        assertTrue(like);

        boolean unlike = postingListService.unLike(postingId, userId);
        assertTrue(unlike);
        assertFalse(postingListService.isLike(postingId, userId));

        long postingLikeCount = postingListService.getPostingLikeCount(postingId);
        assertEquals(0, postingLikeCount);
    }

    @Test
    @DisplayName("포스팅에 대한 전체 개수 조회")
    void postingListCountTest() {
        postingIds.forEach(postingId -> postingListService.like(postingId, userId));

        long postingListLikeCount = postingListService.getPostingListLikeCount(postingIds);

        assertEquals(postingIds.size(), postingListLikeCount);
    }

}