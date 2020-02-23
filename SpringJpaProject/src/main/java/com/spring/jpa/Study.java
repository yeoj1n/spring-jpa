package com.spring.jpa;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor @AllArgsConstructor
@Getter @Setter
public class Study {

	@Id @GeneratedValue
	private Long id;
	
	private String name;
	
	// Study 입장: 한 사람이 많은 스터디를 만들 수 있다.
	@ManyToOne
	private Account owner;
	
}
