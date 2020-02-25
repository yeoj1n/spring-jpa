package com.spring.jpa;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.Repository;

@NoRepositoryBean
public interface CommonRepository<T, ID extends Serializable> extends Repository<T, ID>{
	//Repository : 아무 기능이 없는 마크 인터페이스
	
	<E extends T>E save(E entity);
	
	List<T> findAll();
	
	Long count();

}
  