package com.spring.jpa.board.dto;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor @AllArgsConstructor
@Getter @Setter
@Table(name = "board")
@Entity
public class Board {

	@Id @GeneratedValue
	private Long id;
	
	private String author;
	
	private String title;
	
	private String content;
	

	@Column(name="created_at")
    @CreationTimestamp
    private LocalDateTime createdAt;
	
}
