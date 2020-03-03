package com.spring.jpa;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import org.springframework.data.domain.AbstractAggregateRoot;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor @AllArgsConstructor
@Getter @Setter
@ToString (exclude = "comments")
@Entity
public class Post extends AbstractAggregateRoot<Post>{

	@Id @GeneratedValue
	private Long id;
	
	private String title;
	
	@OneToMany(mappedBy = "post", cascade = CascadeType.PERSIST)
	private Set<Comment> comments = new HashSet<>();
	
	public void addComment(Comment comment) {
		this.getComments().add(comment);
		comment.setPost(this);
	}
	
	public void removeComment(Comment comment) {
		this.getComments().remove(comment);
		comment.setComment(null);
	}
	
	// save 시 publish 해주면 이벤트를 발생시킨다.
	public Post publish() {
		
		// 이벤트 등록
		this.registerEvent(new PostPublishedEvent(this));
		return this;
	}
}

