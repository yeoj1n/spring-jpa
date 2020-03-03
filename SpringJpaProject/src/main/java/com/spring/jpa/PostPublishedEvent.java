package com.spring.jpa;

import org.springframework.context.ApplicationEvent;

import lombok.Getter;

// Post를 발행하는 event 클래스
public class PostPublishedEvent extends ApplicationEvent{

	@Getter
	private final Post post;
	
	public PostPublishedEvent(Object source) {
		super(source);
		this.post = (Post) source;
	}
}
