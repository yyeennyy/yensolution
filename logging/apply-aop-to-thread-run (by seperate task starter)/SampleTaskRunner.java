package com.example.demo;

import org.springframework.stereotype.Component;

@Component
public class SampleTaskRunner {

    // 아래 runTask 는 AOP 대상이다.
    // 또한, runTask 에서는 task.run()을 직접 호출하고 있는데,
    // start()가 아닌 run() 자체는 동기호출이므로
    // run()이 끝날 때 runTask()또한 끝나게 된다.
    // 따라서 runTask()의 시작과 끝을 AOP로 감지할 수 있게 된다.

    public void runTask(Runnable task) {
        task.run();
    }

//    [주의점]
//    위 runTask를 AOP 대상으로 했을 때
//    아래와 같이 runTask를 동일 클래스 (동일 빈)에서 호출하면 AOP 적용 불가하다.
//    프록시로 감싸진 메서드만 AOP의 대상이기 때문이다.
//    즉 다른 빈에서 runTask를 호출해야 AOP의 감지 대상이 된다.

//    public void startTask(SampleThread sampleThread) {
//        Thread t = new Thread(() -> runTask(sampleThread));
//        t.start();
//    }

}
