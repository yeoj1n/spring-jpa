package com.spring.jpa;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.Repository;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

@NoRepositoryBean
public interface CommonRepository<T, ID extends Serializable> extends Repository<T, ID>{
	//Repository : 아무 기능이 없는 마크 인터페이스
	
	<E extends T>E save(@NonNull E entity);
	
	List<T> findAll();
	
	Long count();

	@Nullable
	<E extends T>Optional<E> findById(ID id);
}