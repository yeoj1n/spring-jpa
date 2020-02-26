package com.spring.jpa.board.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.spring.jpa.board.dto.Board;
import com.spring.jpa.board.repository.BoardRepository;

@RestController
@RequestMapping("/board")
public class BoardController {
	
	@Autowired
	private BoardRepository boardRepository;

	// 전체 게시글 리스트
	@GetMapping("/list")
	public List<Board> getBoardList() {
		List<Board> boardList = boardRepository.findAll();
		return boardList;
	}
	
	// 10개씩 페이징
	@GetMapping("/list/{pageNum}")
	public Page<Board> getBoardList(@PathVariable("pageNum")int pageNum) {
		Page<Board> boardList = boardRepository.findAll(PageRequest.of(pageNum, 10));
		return boardList;
	}
	
	@GetMapping("/{id}")
	public Board getBoard(@PathVariable("id")long id) {
		Optional<Board> board = boardRepository.findById(id);
		return board.get();
	}
	
	@PostMapping
	public void createBoard(Board board) {
		boardRepository.save(board);
	}
	
	@PutMapping
	public void updateBoard(Board newBoard) {
		Board board = boardRepository.findById(newBoard.getId()).get();
		board.setAuthor(newBoard.getAuthor());
		board.setTitle(newBoard.getTitle());
		board.setContent(newBoard.getContent());
		
		boardRepository.save(board);
	}
	
	@DeleteMapping("/{id}")
	public void removeBoard(@PathVariable("id")long id) {
		boardRepository.deleteById(id);
	}
	
}
