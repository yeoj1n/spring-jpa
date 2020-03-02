package com.spring.jpa;


import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@DataJpaTest
public class PostRepositoryTest {

	@Autowired
	PostRepository postRepository;
	
//	@Test
//	@Rollback(false)
//	public void crudRepositoy(){
//		// Given
//		Post post = new Post();
//		post.setTitle("hello spring boot common!");
//		assertThat(post.getId()).isNull();
//		
//		// When
//		Post newPost = postRepository.save(post);
//		List<Post> posts = postRepository.findAll();
//		
//		//Then
//		assertThat(posts.size()).isEqualTo(1);
//		assertThat(posts.contains(newPost));
//	
//		// When
//		Page<Post> page = postRepository.findAll(PageRequest.of(0, 10));
//		
//		//Then
//		assertThat(page.getTotalElements()).isEqualTo(1);
//		assertThat(page.getNumber()).isEqualTo(0);// page는 0부터 시작
//		assertThat(page.getTotalPages()).isEqualTo(1);
//		assertThat(page.getSize()).isEqualTo(10);
//		assertThat(page.getNumberOfElements()).isEqualTo(1);
//		
//		//When
//		page = postRepository.findByTitleContains("spring",PageRequest.of(0, 10));
//		
//		//Then
//		assertThat(page.getTotalElements()).isEqualTo(1);
//		assertThat(page.getNumber()).isEqualTo(0);// page는 0부터 시작
//		assertThat(page.getTotalPages()).isEqualTo(1);
//		assertThat(page.getSize()).isEqualTo(10);
//		assertThat(page.getNumberOfElements()).isEqualTo(1);
//		
//		//When
//		long count = postRepository.countByTitleContains("spring");
//		
//		//Then
//		assertThat(count).isEqualTo(1);
//	}
	
	// ---------- 커스텀 레포지토리 ----------
//	@Test
//	@Rollback(false)
//	public void crudRepositoy(){
//		//postRepository.findMyPost();
//		
//		Post post = new Post();
//		post.setTitle("hibernate");
//		postRepository.save(post);
//		
//		postRepository.delete(post);
//		// postRepository.delete만 하면 rollaback 때문에 delete 하지않음, delete 를 하고 싶다면 flush 추가
//		postRepository.flush();
//		
//		// insert, delete 쿼리 날아가지 않는 이유 : 최종 쿼리만 실행 (rollback)
//	}
	
	// ---------- 커스텀 레포지토리 ----------
	
	
	// ---------- 기본 레포지토리 커스터마이징 (SimpleMyRepository) ----------
	
	@Test
	public void crud() {
		Post post = new Post();
		post.setTitle("post 1");
		
		// 저장 전이므로 post 객체의 상태는 Transient(JPA가 모르는) 상태
		assertThat(postRepository.contains(post)).isFalse();
		
		postRepository.save(post);
		
		// 저장 후므로 post 객체의 상태는 Persist(JPA가 관리중인) 상태
		assertThat(postRepository.contains(post)).isTrue();
	
		
		postRepository.delete(post);
		postRepository.flush(); 
		//flush 하면  삭제 후이므로 post 상태는 Detach(JPA가 관리하지 않는) 상태
	}
	
	// ---------- 기본 레포지토리 커스터마이징 (SimpleMyRepository) ----------
}
