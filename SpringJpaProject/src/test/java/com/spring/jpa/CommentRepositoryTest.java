package com.spring.jpa;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@DataJpaTest
public class CommentRepositoryTest {

	@Autowired
	CommentRepository commentRepository;
	

	@Test
	public void crud() {
		Comment comment = new Comment();
		comment.setComment("this is comment");
		
		commentRepository.save(comment);
		
		// When
		List<Comment> comments = commentRepository.findAll();

		// Then
		assertThat(comments.size()).isEqualTo(1);
		
		// When
		Long count = commentRepository.count();
		
		// Then
		assertThat(count).isEqualTo(1);
		
		// ----------------- Null 처리하기 -----------------
		Optional<Comment> byId = commentRepository.findById(100L);
		byId.orElse(null);
		byId.orElseThrow(() -> new NoSuchElementException());
		
		commentRepository.save(null);
		
		if(comments != null) // 올바르지 않은 방법
		assertThat(comments).isEmpty();
		
		// ----------------- Null 처리하기 -----------------
	}
	
}
