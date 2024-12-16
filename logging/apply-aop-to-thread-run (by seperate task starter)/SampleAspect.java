package com.example.demo;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class SampleAspect {
    @Around("execution(* com.example.demo.SampleTaskRunner.runTask(..))")
    public Object aroundRunTask(ProceedingJoinPoint joinPoint) throws Throwable {
        System.out.println("작업을 시작합니다.");
        Object result = joinPoint.proceed();
        System.out.println("작업을 완료했습니다.");
        return result;
    }
}