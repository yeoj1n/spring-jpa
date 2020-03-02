package com.spring.jpa;

import java.util.*;

public interface PostCustomRepository<T> {

	List<Post> findMyPost(); // Spring Data Repository 인터페이스에 기능 추가
	void delete(T entity); // Spring Data Repository 기본 기능 덮어쓰기
}
