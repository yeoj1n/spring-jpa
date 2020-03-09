package com.spring.jpa;



import static org.assertj.core.api.Assertions.assertThat;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

import com.querydsl.core.types.Predicate;

@RunWith(SpringRunner.class)
@DataJpaTest
@Import(PostRepositoryTestConfig.class) // 이벤트 리스너 bean 을 주입
public class PostRepositoryTest {

	@Autowired
	PostRepository postRepository;
	
	@Autowired
	CommentRepository commentRepository;
	
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
//		Page<Post> page = postRepository.findAll (PageRequest.of(0, 10));
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
	
//	@Test
//	public void crud() {
//		Post post = new Post();
//		post.setTitle("post 1");
//		
//		// 저장 전이므로 post 객체의 상태는 Transient(JPA가 모르는) 상태
//		assertThat(postRepository.contains(post)).isFalse();
//		
//		postRepository.save(post);
//		
//		// 저장 후므로 post 객체의 상태는 Persist(JPA가 관리중인) 상태
//		assertThat(postRepository.contains(post)).isTrue();
//	
//		
//		postRepository.delete(post);
//		postRepository.flush(); 
//		//flush 하면  삭제 후이므로 post 상태는 Detach(JPA가 관리하지 않는) 상태
//	}
	
	// ---------- 기본 레포지토리 커스터마이징 (SimpleMyRepository) ----------
	
	// ---------- 도메인 이벤트 ----------
	@Autowired
	ApplicationContext applicationContext;

//	// 스프링 프레임워크의 이벤트 발생 기능 (applicationContext 이용)
//	@Test
//	public void event() {
//		Post post = new Post();
//		post.setTitle("event");
//		
//		PostPublishedEvent event = new PostPublishedEvent(post);
//		applicationContext.publishEvent(event);
//		
//	}
	
	// 스프링 데이터의 도메인 이벤트 발생 기능(save 시 이벤트 발생 , Entity 에 AbstractAggregateRoot를 상속, publish 메소드 등록)
//	@Test
//	public void event() {
//		Post post = new Post();
//		post.setTitle("event");
//		
//		// 이벤트 발행
//		postRepository.save(post.publish());
//	}
	// ---------- 도메인 이벤트 ----------
	
	
	// ---------- QueryDSL : custom repository ----------
	
	@Test
	public void crud() {
		Comment comment1 = new Comment();
		comment1.setComment("test comment1!");
		comment1.setLikeCount(5);
		
		Comment comment2 = new Comment();
		comment2.setComment("test comment2!");
		comment2.setLikeCount(3);
		
		commentRepository.save(comment1);
		commentRepository.save(comment2);
		
		
		Set<Comment> set = new HashSet<>();
		set.add(comment1);
		set.add(comment2);
		
		Post post = new Post();
		post.setTitle("hibernate!");
		post.setComments(set);
		
		postRepository.saveAndFlush(post.publish());
		
		Predicate predicate = QPost.post.title.contains("hi");
		Optional<Post> one = postRepository.findOne(predicate);
		assertThat(one).isNotEmpty();
	}
	
	// ---------- QueryDSL : custom repository ----------
}
