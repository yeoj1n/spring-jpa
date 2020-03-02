package com.spring.jpa;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.stream.Stream;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.lang.Nullable;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

@RunWith(SpringRunner.class)
@DataJpaTest
public class CommentRepositoryTest {

	@Autowired
	CommentRepository commentRepository;
	

	@Test
	public void crud() throws InterruptedException, ExecutionException {
//		Comment comment = new Comment();
//		comment.setComment("this is comment");
//		
//		commentRepository.save(comment);
//		
//		// When
//		List<Comment> comments = commentRepository.findAll();
//
//		// Then
//		assertThat(comments.size()).isEqualTo(1);
//		
//		// When
//		Long count = commentRepository.count();
//		
//		// Then
//		assertThat(count).isEqualTo(1);
//		
//		// ----------------- Null 처리하기 -----------------
//		Optional<Comment> byId = commentRepository.findById(100L);
//		byId.orElse(null);
//		byId.orElseThrow(() -> new NoSuchElementException());
//		
//		commentRepository.save(null);
//		
//		if(comments != null) // 올바르지 않은 방법
//		assertThat(comments).isEmpty();
//		
		// ----------------- Null 처리하기 -----------------
		
		// ----------------- 쿼리 만들기 ----------------- 
		Comment comment1 = new Comment();
		comment1.setComment("test1");
		comment1.setLikeCount(2);
		
		Comment comment2 = new Comment();
		comment2.setComment("test2");
		comment2.setLikeCount(10);
		
		
		commentRepository.save(comment1);
		commentRepository.save(comment2);
//
//		
//		Page<Comment> pages = commentRepository.findByLikeCountGreaterThan(2, PageRequest.of(0, 10));
//		
//		pages.forEach(c -> System.out.println(c.getComment()));
		// ----------------- 쿼리 만들기 -----------------
		
		// ----------------- 쿼리 만들기 실습-----------------
		
		//List<Comment> comments = commentRepository.findByCommentContainsIgnoreCaseAndLikeCountGreaterThan("test", 1);
		//assertThat(comments.size()).isEqualTo(2);
		
		
//		try(Stream<Comment> comments = commentRepository.findByCommentContainsIgnoreCaseAndLikeCountGreaterThan("test", 1)) {
//			Comment firstComment = comments.findFirst().get();
//			//assertThat(firstComment.getComment()).isEqualTo("test1");
//			System.out.println(firstComment.getComment());
//		}
		
		
		// ----------------- 쿼리 만들기 실습-----------------	
		
		// ----------------- 비동기 쿼리-----------------
	
//		// non-blocking call
//		Future<List<Comment>> future = commentRepository.findByCommentContainsIgnoreCase("test",PageRequest.of(0, 10));
//		System.out.println("done? " + future.isDone());
//		
//		// get() : 올 때까지 기다리는 get
//		List<Comment> comments = future.get(); // spring의 async가 제대로 동작하지 않는다면 non-blocking call이라고 말할 수 없음 (insert 쿼리가 날아감)
//		comments.forEach(System.out::println);
		
		// ListenableFuture : callback을 등록하여 사용할 수 있다. -> 작업이 마무리가 된 후 실행
		ListenableFuture<List<Comment>> future = commentRepository.findByCommentContainsIgnoreCase("test",PageRequest.of(0, 10));
		
		System.out.println("done? " + future.isDone());
		
		// insert 쿼리 날아가지 않은 것을 볼 수 있다.
		future.addCallback(new ListenableFutureCallback<List<Comment>>() {
			
			@Override
			public void onSuccess(@Nullable List<Comment> result) {
				result.forEach(System.out::println);
			}
			
			@Override
			public void onFailure(Throwable ex) {
				System.out.println(ex);
			}
		});
		
		// ----------------- 비동기 쿼리-----------------
		
	}
	
}
