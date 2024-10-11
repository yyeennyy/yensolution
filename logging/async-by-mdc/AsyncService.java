package com.example.demo;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class AsyncService {

    @Async
    public void startAsyncSomething() {
        Util.sleepRandomly();

        // 로깅부
        String name = MDC.get("requestName");
        long end = Long.parseLong(MDC.get("endTime"));
        long start = Long.parseLong(MDC.get("startTime"));
        log.info("thread {}: processing time {}", name, end - start);
    }
}