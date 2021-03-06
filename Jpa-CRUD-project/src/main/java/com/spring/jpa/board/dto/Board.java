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

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor(access = AccessLevel.PROTECTED) @AllArgsConstructor
@Getter @Setter
@Table(name = "board")
@ToString
@Entity
public class Board {

	@Id @GeneratedValue
	private Long id;
	
	private String author;
	
	private String title;
	
	private String content;
	
	@Column(name="created_at", nullable = true)
    @CreationTimestamp
    private LocalDateTime createdAt;
	
//	@Column(name="updated_at", nullable = false)
//	@LastModifiedDate
//    private LocalDateTime updatedAt;
	
	@Builder
	public Board(String title, String content, String author, Long id, LocalDateTime createdAt) {
		this.title = title;
		this.content = content;
		this.author = author;
		this.id = id;
		this.createdAt = createdAt;
	}
	
}
