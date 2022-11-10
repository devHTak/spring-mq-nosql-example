package com.example.chap01;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.util.concurrent.ListenableFutureCallback;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ProducerController {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    @GetMapping("/api/select")
    public void selectColor(@RequestHeader("user-agent") String userAgentName, @RequestParam String userName, @RequestParam String colorName) {
        UserEvent event = UserEvent.builder()
                .userName(userName)
                .userAgent(userAgentName)
                .colorName(colorName)
                .timestamp(LocalDateTime.now())
                .build();

        String record = "";
        try {
            record = objectMapper.writeValueAsString(event);
        } catch (JsonProcessingException e) {
            log.error("ProducerController exception: {}", e.getMessage());
        }

        kafkaTemplate.send("select-color", record)
                .addCallback(new ListenableFutureCallback<SendResult<String, String>>() {
                    @Override
                    public void onFailure(Throwable ex) { log.error("Producer send error : {}", ex.getMessage()); }
                    @Override
                    public void onSuccess(SendResult<String, String> result) { log.info("Producer success: {}", result.toString()); }
                });

    }
}
