package com.spring.jpa;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor @AllArgsConstructor
@Getter @Setter
@ToString
@Entity
public class Comment {

	@Id @GeneratedValue
	private Long id;
	
	private String comment;
	
	private Integer likeCount;
	
	@ManyToOne
	private Post post;
}
