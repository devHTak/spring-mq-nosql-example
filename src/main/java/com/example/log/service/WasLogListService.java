package com.example.log.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
public class WasLogListService {
    private static final String LOG_FILE_NAME_PREFIX = "./waslog";
    private static final String KEY_WAS_LOG = "was:log:list";
    private final RedisTemplate<String, String> redisTemplate;
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd HH:mm:ss");

    @Autowired
    public WasLogListService(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    /**
     * 레디스에 로그 기록
     */
    public void log(String log) {
        ListOperations<String, String> listOperations = redisTemplate.opsForList();
        listOperations.rightPush(KEY_WAS_LOG, getLog(log));
    }

    private String getLog(String log) {
        return new StringBuilder()
                .append(simpleDateFormat.format(new Date()))
                .append(": ")
                .append(log)
                .append("\n")
                .toString();
    }

    public long writeLog() {
        ListOperations<String, String> listOperations = redisTemplate.opsForList();
        List<String> range = listOperations.range(KEY_WAS_LOG, 0, -1);
        listOperations.getOperations().delete(KEY_WAS_LOG);

        return range.stream().map(log -> writeFile(log)).count();
    }

    private String writeFile(String log) {
        String fileName = getCurrentFileName();
        try {
            FileWriter fileWriter = new FileWriter(fileName, true);
            fileWriter.write(log);
            fileWriter.flush();
        } catch(Exception e) {
            e.printStackTrace();
        }

        return fileName;
    }

    private String getCurrentFileName() {
        return LOG_FILE_NAME_PREFIX + simpleDateFormat.format(new Date());
    }
}
