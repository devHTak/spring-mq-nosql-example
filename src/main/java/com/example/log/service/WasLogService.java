package com.example.log.service;

import com.example.log.entity.WasLog;
import com.example.log.repository.WasLogRepository;
import io.lettuce.core.internal.LettuceLists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class WasLogService {
    private final WasLogRepository wasLogRepository;
    private static final String LOG_FILE_NAME_PREFIX = "./waslog";

    @Autowired
    public WasLogService(WasLogRepository wasLogRepository) {
        this.wasLogRepository = wasLogRepository;
    }

    public WasLog logWriter(WasLog wasLog) {
        return wasLogRepository.save(wasLog);
    }

    public long logReceiver() {
        long result = 0;
        for (WasLog wasLog : wasLogRepository.findAll()) {
            if(wasLog != null) {
                this.writeFile(wasLog);
                wasLogRepository.delete(wasLog);

                result += 1;
            }
        }

        return result;
    }

    private void writeFile(WasLog wasLog) {
        try(FileWriter fileWriter = new FileWriter(getCurrentFileName(), true)) {
            fileWriter.write(wasLog.toString());
            fileWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String getCurrentFileName() {
        return LOG_FILE_NAME_PREFIX + LocalDate.now();
    }
}
