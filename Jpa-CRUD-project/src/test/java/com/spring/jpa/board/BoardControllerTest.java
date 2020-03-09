package com.spring.jpa.board;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.spring.jpa.board.dto.Board;
import com.spring.jpa.board.repository.BoardRepository;



@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class BoardControllerTest {

	@Autowired
	MockMvc mockMvc;
	
	@Autowired
	BoardRepository boardRepository;
	
//	@Test
//	public void getBoard() throws Exception {
//		Board board = Board.builder()
//				.title("board title1")
//				.content("board content1")
//				.author("test user")
//				.build();
//		boardRepository.save(board);
//		
//		mockMvc.perform(get("/board/" + board.getId()))
//				.andDo(print());
//	}
	
	@Test
	public void getBoards() throws Exception {
		Board board1 = Board.builder()
				.title("title1")
				.content("board content1")
				.author("test user")
				.build();
		
		Board board2 = Board.builder()
				.title("title2")
				.content("board content2")
				.author("test user")
				.build();
		
		boardRepository.save(board1);
		boardRepository.save(board2);
		
//		mockMvc.perform(get("/board/list")
//				.param("page", "0")
//				.param("size", "10"))
//				.andDo(print())
//				.andExpect(status().isOk());
		
		mockMvc.perform(get("/board/list")
						.param("page", "0")
						.param("size", "10"))
//						.param("sort", "createdAt,desc")
//						.param("sort", "title"))
						.andDo(print())
						.andExpect(status().isOk());
//						.andExpect(jsonPath("$.content[0].title", is("title2")));
	}
	
}
