package com.example.demo;

import java.util.Random;

public class Util {
    public static void sleepRandomly() {
        Random random = new Random();
        int randomTime = random.nextInt(5000) + 1000;

        try {
            Thread.sleep(randomTime);
        } catch (Exception e) {
            System.err.println("exception: " + e.getMessage());
        }
    }
}