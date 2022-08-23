package com.example.log.service;

import com.example.log.entity.WasLog;
import com.example.log.repository.WasLogRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class WasLogServiceTest {

    @Autowired
    private WasLogService wasLogService;
    @Autowired
    private WasLogRepository wasLogRepository;

    @Test
    @DisplayName("WAS LOG 데이터 저장 후 읽고 파일 저장")
    void wasLogReadAndWrite() {
        // 로그 저장 확인
        List<WasLog> logs = Arrays.asList(
            new WasLog("WAS-1", "SERVER START", LocalDateTime.now()),
            new WasLog("WAS-2", "SERVER START", LocalDateTime.now()),
            new WasLog("WAS-1", "SERVER END", LocalDateTime.now()),
            new WasLog("WAS-2", "SERVER END", LocalDateTime.now())
        );

        List<WasLog> results = logs.stream().map(log -> wasLogService.logWriter(log))
                .collect(Collectors.toList());

        System.out.println(logs.size() + " " + results.size());
        assertEquals(logs.size(), results.size());

        // 쓰기 테스트
        long writeResult = wasLogService.logReceiver();
        assertEquals(logs.size(), writeResult);
    }

}