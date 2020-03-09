package com.spring.jpa.board;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
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
	
//	@Test
//	public void crudTest() {
//		Board board = Board.builder()
//						.title("board title1")
//						.content("board content1")
//						.author("test user")
//						.build();
//		Board board2 = Board.builder()
//						.title("board title2")
//						.content("board content2")
//						.author("test user")
//						.build();
//		
//		// 저장
//		boardRepository.save(board);
//		
//		boardRepository.save(board2);
//		
//		// board list 확인
//		List<Board> lists = boardRepository.findAll();
//		lists.forEach(System.out::println);
//		
//		Page<Board> page = boardRepository.findAll(PageRequest.of(0, 10));
//		assertThat(page.getTotalElements()).isEqualTo(2);
//		
//		System.out.println("------------------------------- 업데이트 전 -------------------------------");
//		System.out.println(board.toString());
//	
//		
//		//update
//		boardRepository.save(board.builder()
//								.title("update title")
//								.content("update content")
//								.author(board.getAuthor())
//								.build());
//				
//		System.out.println("------------------------------- 업데이트 후 -------------------------------");
//		System.out.println(board.toString());
		
		
		@Test
		public void crudTest() {
			
			Board board = Board.builder()
					.title("board title1")
					.content("board content1")
					.author("test user")
					.build();
			
			// save 테스트
			boardRepository.save(board);
			
			// contains 테스트
			Integer containNum = boardRepository.contains(1L);
			
			if(containNum > 0) {
				System.out.println("exist!!");
			}
			System.out.println("Not exist!!");
			
			boardRepository.flush();
			
			
			// update 테스트
			long id = board.getId();
			LocalDateTime createTime = board.getCreatedAt();
			
			System.out.println("update 전!!!!!");
			System.out.println(boardRepository.findById(id).get().getTitle());
			
			Board newBoard = Board.builder()
					.title("update title1")
					.content("update content1")
					.author("update user")
					.createdAt(createTime)
					.id(id)
					.build(); 
			
			boardRepository.save(newBoard);
			
			System.out.println("update 후!!!!!");
			System.out.println(boardRepository.findById(id).get().getTitle());
		}
	
}
