package com.spring.jpa.board.dto;

import java.time.LocalDateTime;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.PreUpdate;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.annotation.LastModifiedDate;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor(access = AccessLevel.PROTECTED) @AllArgsConstructor
@Getter
@Table(name = "board")
@ToString
@Entity
public class Board {

	@Id @GeneratedValue
	private Long id;
	
	private String author;
	
	private String title;
	
	private String content;
	
	@Column(name="created_at", nullable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;
	
//	@Column(name="updated_at", nullable = false)
//	@LastModifiedDate
//    private LocalDateTime updatedAt;
	
	@Builder
	public Board(String title, String content, String author) {
		this.title = title;
		this.content = content;
		this.author = author;
	}
	
}
