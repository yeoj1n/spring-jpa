package com.spring.jpa;

import java.util.List;

import org.springframework.data.repository.RepositoryDefinition;

//Repository interface 정의하기 -> 1. @RepositoryDefinition 인터페이스로 메소드를 직접 정의
//@RepositoryDefinition(domainClass=Comment.class, idClass=Long.class)
//public interface CommentRepository {
//	
//	Comment save(Comment comment);
//	List<Comment> findAll();
//	Long count();
//}


//Repository interface 정의하기 -> 2. 공통 인터페이스 상속
public interface CommentRepository extends CommonRepository<Comment, Long> {}