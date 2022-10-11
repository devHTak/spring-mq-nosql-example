package com.example.redis.log.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class WasLogService {
    private static final String LOG_FILE_NAME_PREFIX = "./waslog";
    private static final String KEY_WAS_LOG = "was:log";
    private final RedisTemplate<String, String> redisTemplate;
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd HH:mm:ss");

    @Autowired
    public WasLogService(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    /**
     * 레디스에 로그 기록
     */
    public void log(String log) {
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        valueOperations.set(KEY_WAS_LOG, getLog(log));
    }

    private String getLog(String log) {
        return new StringBuilder()
                .append(simpleDateFormat.format(new Date()))
                .append(": ")
                .append(log)
                .append("\n")
                .toString();
    }

    public String writeLog() {
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        String log = valueOperations.get(KEY_WAS_LOG);

        return writeFile(log);
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
