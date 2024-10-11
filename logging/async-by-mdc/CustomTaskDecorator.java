package com.example.demo;

import org.slf4j.MDC;
import org.springframework.core.task.TaskDecorator;

import java.util.Map;

public class CustomTaskDecorator implements TaskDecorator {

    // - 요청된 Runnable 작업을 데코레이트한다.
    // - 주어진 Runnable 작업을 감싸서, 새로운 Runnable을 반환한다.
    // - 현재 쓰레드의 MDC 컨텐스트 맵을 저장하고, 새로운 쓰레드에서 복원 (쓰레드 전환시에도 컨텍스트를 유지 가능)
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