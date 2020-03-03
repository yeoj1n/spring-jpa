package com.spring.jpa;

import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;

// 이벤트 listener class (PostRepositoryTestConfig.class에서 bean으로 등록)
//public class PostListener implements ApplicationListener<PostPublishedEvent>{
//	
//	// 이벤트 발생 시 해야할 일
//	@Override
//	public void onApplicationEvent(PostPublishedEvent event) {
//		System.out.println("-------------------------------");
//		System.out.println(event.getPost() + "is published!");
//		System.out.println("-------------------------------");
//	}
//
//}


// ApplicationListener 상속대신 메소드에 @EventListener 어노테이션 등록 
public class PostListener {
	
	@EventListener
	public void onApplicationEvent(PostPublishedEvent event) {
		System.out.println("-------------------------------");
		System.out.println(event.getPost() + "is published!");
		System.out.println("-------------------------------");
	}
}
