package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CustomThreadFactory {
    @Autowired
    private AsyncService asyncService;

    public CustomThread createThread() {
        return new CustomThread(asyncService);
    }
}