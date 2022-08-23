package com.example.log;

import com.example.log.entity.WasLog;
import com.example.log.service.WasLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
public class WasLogController {

    private final WasLogService wasLogService;

    @Autowired
    public WasLogController(WasLogService wasLogService) {
        this.wasLogService = wasLogService;
    }

    @PostMapping("/logs")
    public ResponseEntity<WasLog> createWasLog(@RequestBody WasLog wasLog) {
        wasLog.setCreatedAt(LocalDateTime.now());
        WasLog result = wasLogService.logWriter(wasLog);

        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    @GetMapping("/logs/write")
    public ResponseEntity<String> writeFileLog() {
        long result = wasLogService.logReceiver();

        return ResponseEntity.ok("SUCCESS " + result);
    }

}
