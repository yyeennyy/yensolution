package com.example.demo;

import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;
import java.util.concurrent.Executor;

@RestController
public class TestController {

    private final Executor taskExecutor;

    public TestController(@Qualifier("taskExecutor") Executor taskExecutor) {
        this.taskExecutor = taskExecutor;
    }


    @GetMapping("/log-test")
    public String startThread() {
        MDC.put("requestName", String.valueOf(UUID.randomUUID()));
        MDC.put("startTime", String.valueOf(System.nanoTime()));
        taskExecutor.execute(new CustomThread());

        return "[api] thread started";
    }
}