package com.example.demo;

import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;
import java.util.concurrent.Executor;

@RestController
public class TestController {

    private final Executor taskExecutor;
    private final AsyncService asyncService;

    public TestController(@Qualifier("taskExecutor") Executor taskExecutor, AsyncService asyncService) {
        this.taskExecutor = taskExecutor;
        this.asyncService = asyncService;
    }

    @GetMapping("/log-test")
    public String startThread() {
        MDC.put("requestName", String.valueOf(UUID.randomUUID()));
        MDC.put("startTime", String.valueOf(System.nanoTime()));
        taskExecutor.execute(new CustomThread(asyncService));

        return "[api] thread started";
    }
}