package com.spring.jpa;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;

import com.spring.jpa.board.dto.Board;
import com.spring.jpa.board.repository.BoardRepository;

@RunWith(SpringRunner.class)
@DataJpaTest
public class BoardRepositoryTest {

	@Autowired
	BoardRepository boardRepository;
	
	@Test
	public void crudTest() {
		Board board = new Board();
		board.setTitle("board title1");
		board.setContent("board content1");
		board.setAuthor("test user");
		
		Board board2 = new Board();
		board.setTitle("board title2");
		board.setContent("board content2");
		board.setAuthor("test user");
		
		// 저장
		boardRepository.save(board);
		boardRepository.save(board2);
		
		// board list 확인
		List<Board> lists = boardRepository.findAll();
		lists.forEach(System.out::println);
		
		Page<Board> page = boardRepository.findAll(PageRequest.of(0, 10));
		assertThat(page.getTotalElements()).isEqualTo(2);
		
		board.setTitle("update title");
		
		//update
		boardRepository.save(board);
		
		
	}
	
}
