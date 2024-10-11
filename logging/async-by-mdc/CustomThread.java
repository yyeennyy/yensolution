package com.example.demo;

import org.slf4j.MDC;

public class CustomThread extends Thread{

    public void run() {
        Util.sleepRandomly();
        MDC.put("endTime", String.valueOf(System.nanoTime()));

        AsyncService asyncService = new AsyncService();
        asyncService.startAsyncSomething();
    }
}