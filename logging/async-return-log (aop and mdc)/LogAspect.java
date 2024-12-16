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

    // 무효한 Aspect 이다. 작동 불가능하다.
    // 이 예제에서는 new CustomThread 한 것을 taskExecutor.execute()를 통해 실행하고 있는데,
    // new는 Spring context와 무관하게 객체를 생성한 것이다.
    // 그런 객체의 run()을 호출하는 것은 프록시로 감싸진 것이 아니라서 AOP 대상이 되지 못한다.
    @AfterReturning("execution(* com.example.demo.CustomThread.run(..))")
    public void afterThread() {
        log.info("req {}: other thread started", MDC.get("requestName"));
    }

    // MDC 컨텍스트가 잘 유지되었나 보기 위한 부분이다.
    // 포인트컷에 지정한 저 메서드는 Spring의 @Async 기능을 사용하여 프록시로 감싸져 호출되는 메서드다.
    // 그러므로 AOP의 대상이 되고, 아래 advice가 동작한다.
    @AfterReturning("execution(* com.example.demo.AsyncService.startAsyncSomething(..))")
    public void afterAsyncMethod() {
        String name = MDC.get("requestName");
        long end = Long.parseLong(MDC.get("endTime"));
        long start = Long.parseLong(MDC.get("startTime"));
        log.info("req {}: processing time {}", name, end - start);
    }
}