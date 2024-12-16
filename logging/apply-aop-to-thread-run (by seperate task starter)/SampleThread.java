package com.example.demo;

import lombok.SneakyThrows;

public class SampleThread extends Thread {

    // 비동기 작업 샘플

    @SneakyThrows
    public void run() {
        for (int i=0; i<5; i++) {
            System.out.println("something async process..");
            Thread.sleep(1000);
        }
    }
}