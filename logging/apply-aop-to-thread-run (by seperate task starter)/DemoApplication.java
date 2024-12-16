package com.example.demo;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@RequiredArgsConstructor
@SpringBootApplication
public class DemoApplication {

    public static void main(String[] args) {
		ConfigurableApplicationContext context = SpringApplication.run(DemoApplication.class, args);
		SampleTaskStarter taskStarter = context.getBean(SampleTaskStarter.class);

		/** [사용 예제]
		* AOP 감지 대상 (비돋기) 쓰레드를 시작하는 taskStarter 를 호출한다.
		* 클라이언트는 t.start() 따위가 아닌, taskStarter 로 호출해야 해당 쓰레드를 감지할 수 있다.
	    */
		taskStarter.startTask(new SampleThread());
	}
}