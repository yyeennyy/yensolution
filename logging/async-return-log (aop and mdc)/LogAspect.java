package com.example.demo;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class LogAspect {

    @AfterReturning("execution(* com.example.demo.CustomThread.run(..))")
    public void afterThread() {
        log.info("req {}: other thread started", MDC.get("requestName"));
    }

    @AfterReturning("execution(* com.example.demo.AsyncService.startAsyncSomething(..))")
    public void afterAsyncMethod() {
        String name = MDC.get("requestName");
        long end = Long.parseLong(MDC.get("endTime"));
        long start = Long.parseLong(MDC.get("startTime"));
        log.info("req {}: processing time {}", name, end - start);
    }
}