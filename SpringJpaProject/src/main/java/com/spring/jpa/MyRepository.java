package com.spring.jpa;

import java.io.Serializable;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

//모든 repository에 공통으로 적용할 repository interface
@NoRepositoryBean // JpaRepository를 상속받을 것이기 때문에 추가되는 repository이므로 중복 Bean등록을 막기위해 @NoRepositoryBean 필수
public interface MyRepository<T, ID extends Serializable> extends JpaRepository<T, ID> {
	
	// entity가 Persistent context에 들어있는지 확인하는 기능
	boolean contains(T entity);
	
}
