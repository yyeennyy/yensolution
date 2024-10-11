package com.example.demo;

import org.slf4j.MDC;

public class CustomThread extends Thread{

    private final AsyncService asyncService;

    public CustomThread(AsyncService asyncService) {
        this.asyncService = asyncService;
    }

    public void run() {
        Util.sleepRandomly();
        MDC.put("endTime", String.valueOf(System.nanoTime()));

        asyncService.startAsyncSomething();
    }
}