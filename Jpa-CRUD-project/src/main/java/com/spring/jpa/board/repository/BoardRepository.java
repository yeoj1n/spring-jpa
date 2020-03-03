package com.spring.jpa.board.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.spring.jpa.board.dto.Board;

//public interface BoardRepository extends JpaRepository<Board, Long>{
//
//}

// repository 커스터마이징
public interface BoardRepository extends MyRepository<Board, Long> {}