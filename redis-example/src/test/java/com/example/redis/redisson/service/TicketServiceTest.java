package com.example.redis.redisson.service;

import com.example.redis.redisson.domain.Ticket;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class TicketServiceTest {

    @Autowired
    private TicketService ticketService;
    private String ticketKey;
    private Ticket ticket;

    @BeforeEach
    void beforeEach() {
        final String name = "name";
        final String code = "TESTCODE1234";
        final int quantity = 30;

        ticket = new Ticket(name, code, quantity);
        ticketKey = ticketService.keyResolver(code);
        ticketService.setUsableTicket(ticketKey, quantity);
    }

    @Test
    @DisplayName("락을 사용하여 50명 티켓 예약")
    void reserve50TicketsTest() throws InterruptedException {
        final int numberOfMember = 50;
        final CountDownLatch countDownLatch = new CountDownLatch(numberOfMember);

        List<Thread> threadList = Stream
                .generate(() -> new Thread(()-> {
                    ticketService.reservationTicketWithLock(ticketKey);
                    countDownLatch.countDown();
                }))
                .limit(numberOfMember)
                .collect(Collectors.toList());

        threadList.forEach(Thread::start);
        countDownLatch.await();
    }

}