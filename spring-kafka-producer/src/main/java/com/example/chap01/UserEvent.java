package com.example.chap01;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserEvent {
    private String userName;
    private String colorName;
    private String userAgent;
    private LocalDateTime timestamp;
}
