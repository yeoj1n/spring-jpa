package com.spring.jpa.board.repository;

import java.io.Serializable;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface MyRepository<T, ID extends Serializable> extends JpaRepository<T, ID>{

	Integer contains(ID id);
	
	@Modifying
	void update(T entity);
	
//	@Query(value="update Board b set b.title = :#{#board.title}, b.content = :#{#board.content}, b.author = :#{#board.author}  WHERE b.id = :#{#board.id}", nativeQuery=false)
//	void update(T entity);
	
}
