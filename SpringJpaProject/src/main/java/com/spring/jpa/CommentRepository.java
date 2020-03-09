package com.spring.jpa;

import java.util.List;
import java.util.concurrent.Future;
import java.util.stream.Stream;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.util.concurrent.ListenableFuture;

//Repository interface 정의하기 -> 1. @RepositoryDefinition 인터페이스로 메소드를 직접 정의
//@RepositoryDefinition(domainClass=Comment.class, idClass=Long.class)
//public interface CommentRepository {
//	
//	Comment save(Comment comment);
//	List<Comment> findAll();
//	Long count();
//}


//Repository interface 정의하기 -> 2. 공통 인터페이스 상속
public interface CommentRepository extends CommonRepository<Comment, Long>, QuerydslPredicateExecutor<Comment>{
	
	@Query(value = "SELECT c FROM comment AS c", nativeQuery = true)
	List<Comment> findByCommentContains(String keyword);
	
	Page<Comment> findByLikeCountGreaterThan(int likeCount, Pageable page);
	
	//List<Comment> findByCommentContainsIgnoreCaseAndLikeCountGreaterThan(String keyword, int likeCount);
	
	//Stream<Comment> findByCommentContainsIgnoreCaseAndLikeCountGreaterThan(String keyword, int likeCount);
	
	// Asynchronous : 메소드 실행을 별도의 쓰레드에 위임하는 것
	@Async
	// Future : non-blocking & Asynchronous 하다고 말하기 미흡
	//Future<List<Comment>> findByCommentContainsIgnoreCase(String keyword, Pageable page);
	ListenableFuture<List<Comment>> findByCommentContainsIgnoreCase(String keyword, Pageable page);
	
}