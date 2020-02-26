package com.spring.jpa.board.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.spring.jpa.board.dto.Board;

public interface BoardRepository extends JpaRepository<Board, Long>{

	@Transactional
    @Modifying
	@Query(value="update Board b set b.title = :#{#board.title}, b.content = :#{#board.content}, b.author = :#{#board.author}  WHERE b.id = :#{#board.id}", nativeQuery=false)
	void update(@Param("board") Board board);
}
