package com.spring.jpa.board.controller;

import java.util.List;
import java.util.Optional;

import javax.persistence.PostRemove;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.data.web.PagedResourcesAssemblerArgumentResolver;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.PagedModel;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;


import com.spring.jpa.board.dto.Board;
import com.spring.jpa.board.repository.BoardRepository;



@RestController
@RequestMapping("/board")
public class BoardController {
	
	@Autowired
	private BoardRepository boardRepository;

	// 전체 게시글 리스트 (HATEOAS 사용 전, Paging & sorting)
//	@GetMapping("/list")
//	public Page<Board> getBoardList(Pageable pabeable) {
//		return boardRepository.findAll(pabeable);
//	}
	
	// 리소스 사용(HATEOAS 사용)
	// 2.2.4 기준 : PagedModel<EntityModel<Entity>> / 이전 기준 PagedResources<Resource<Entity>>
	@GetMapping("/list")
	public PagedModel<EntityModel<Board>> getBoardList(Pageable pageable, PagedResourcesAssembler<Board> assembler) {
		return assembler.toModel(boardRepository.findAll(pageable));
	}
	 
//	// 10개씩 페이징
//	@GetMapping("/list/{pageNum}")
//	public Page<Board> getBoardList(@PathVariable("pageNum")int pageNum) {
//		Page<Board> boardList = boardRepository.findAll(PageRequest.of(pageNum, 10));
//		return boardList;
//	}
	
//	@GetMapping("/{id}")
//	public Board getBoard(@PathVariable("id")long id) {
//		Optional<Board> board = boardRepository.findById(id);
//		return board.get();
//	}
	
	
	
	@PostMapping
	public void createBoard(@RequestBody Board board) {
		boardRepository.save(board);
	}
	
	@PutMapping
	public void updateBoard(@RequestBody Board newBoard) {
		Optional<Board> board = boardRepository.findById(newBoard.getId());
		newBoard.setCreatedAt(board.get().getCreatedAt());
		
		board.ifPresent(b -> boardRepository.save(newBoard));
			
			
	}
	 
	@DeleteMapping("/{id}")
	public void removeBoard(@PathVariable("id")long id) {
		boardRepository.deleteById(id);
	}
	
	// contain 체크 후 get 
//	@GetMapping("/{id}")
//	public Board getBoard(@PathVariable("id")long id) {
//		int count = boardRepository.contains(id);
//		if(count != 0) {
//			return boardRepository.findById(id).get();
//		}
//		return null;
//	}
	
	// DomainClassConverter
	@GetMapping("/{id}")
	public Board getBoard(@PathVariable("id")Board board) {
		return board;
	}	
	
}
