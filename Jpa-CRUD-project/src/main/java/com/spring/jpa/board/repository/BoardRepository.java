package com.spring.jpa.board.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.spring.jpa.board.dto.Board;

public interface BoardRepository extends JpaRepository<Board, Long>{
	List<Board> findAll();
	Page<Board> findAll(Pageable pageable);
}
