package com.spring.jpa.board.dto;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

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
	
	@Temporal(TemporalType.TIMESTAMP)
	private String regdate;
	
}
