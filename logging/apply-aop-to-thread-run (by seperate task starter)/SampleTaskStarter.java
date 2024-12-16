package com.example.demo;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class SampleTaskStarter {

    private final SampleTaskRunner taskRunner;

    public void startTask(SampleThread sampleThread) {
        // 이 Bean 에서는 비동기 호출을 한다.

        // AOP 대상인 runTask 를 수행하는 위치는 이렇게 별도의 Bean 이어야 한다.
        // AOP 대상은 프록시로 감싸져 수행되어야 하기 때문이다.
        Thread t = new Thread(() -> taskRunner.runTask(sampleThread));
        t.start();
    }
}