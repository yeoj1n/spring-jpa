package com.spring.jpa;

import java.util.List;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class PostCustomRepositoryImpl implements PostCustomRepository<Post>{
//public class PostCustomRepositoryDefault implements PostCustomRepository<Post>{

	@Autowired
	EntityManager entityManager;
	
	@Override
	public List<Post> findMyPost() {
		return entityManager.createQuery("SELECT p FROM Post AS p", Post.class).getResultList();
	}

	// detached : 한번 persitent 상태였다가 빠져나온 상태, transaction에서 빠져나온 상태로 더이상 persistent 상태가 아님
	@Override
	public void delete(Post post) {
		entityManager.detach(post);// 제거
	}
}
