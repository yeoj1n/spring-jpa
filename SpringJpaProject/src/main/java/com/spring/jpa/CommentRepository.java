package com.spring.jpa;

import java.util.List;
import java.util.stream.Stream;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

//Repository interface 정의하기 -> 1. @RepositoryDefinition 인터페이스로 메소드를 직접 정의
//@RepositoryDefinition(domainClass=Comment.class, idClass=Long.class)
//public interface CommentRepository {
//	
//	Comment save(Comment comment);
//	List<Comment> findAll();
//	Long count();
//}


//Repository interface 정의하기 -> 2. 공통 인터페이스 상속
public interface CommentRepository extends CommonRepository<Comment, Long> {
	
	@Query(value = "SELECT c FROM comment AS c", nativeQuery = true)
	List<Comment> findByCommentContains(String keyword);
	
	Page<Comment> findByLikeCountGreaterThan(int likeCount, Pageable page);
	
	//List<Comment> findByCommentContainsIgnoreCaseAndLikeCountGreaterThan(String keyword, int likeCount);
	
	Stream<Comment> findByCommentContainsIgnoreCaseAndLikeCountGreaterThan(String keyword, int likeCount);
	
}