package com.spring.jpa;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

// @Repository 가 없어도 JpaRepository가 bean으로 등록해준다.
//public interface PostRepository extends JpaRepository<Post, Long> {
//public interface PostRepository extends JpaRepository<Post, Long>, PostCustomRepository<Post>{
//	
//	
////	/* title에 해당 keyword 가 들어있는 post 검색 (10개 단위 페이징) */
////	Page<Post> findByTitleContains(String keyword, Pageable pageable);
////	
////	long countByTitleContains(String keyword);
//	
//	
//}

public interface PostRepository extends MyRepository<Post, Long>, QuerydslPredicateExecutor<Post> {
	
}