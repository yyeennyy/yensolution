package com.example.demo;

import org.slf4j.MDC;
import org.springframework.core.task.TaskDecorator;

import java.util.Map;

public class CustomTaskDecorator implements TaskDecorator {

    @Override
    public Runnable decorate(Runnable task) {
        Map<String, String> callerThreadContext = MDC.getCopyOfContextMap();
        return () -> {
            if (callerThreadContext != null) {
                MDC.setContextMap(callerThreadContext); // 여기에서 복사된 MDC 정보를 설정합니다.
            }
            try {
                task.run();
            } finally {
                MDC.clear();
            }
        };
    }
}