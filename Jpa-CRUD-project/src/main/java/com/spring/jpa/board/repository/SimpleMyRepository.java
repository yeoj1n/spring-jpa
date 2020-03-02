package com.spring.jpa.board.repository;

import java.io.Serializable;

import javax.persistence.EntityManager;

import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;

public class SimpleMyRepository<T, ID extends Serializable> extends SimpleJpaRepository<T, ID> implements MyRepository<T, ID>{

	private EntityManager entityManger;
	
	public SimpleMyRepository(JpaEntityInformation<T, ?> information, EntityManager entityManager) {
		super(information, entityManager);
		this.entityManger = entityManager;
	}
	
	
	@Override
	public Integer contains(ID id) {
		return Integer.parseInt(String.valueOf(entityManger.createNativeQuery("SELECT count(*) from Board where id =" + id).getSingleResult()));
	}
	
	@Override
	public void update(T board) {
		//entityManger.createQuery("update Board b set b.title = :#{#board.title}, b.content = :#{#board.content}, b.author = :#{#board.author}  WHERE b.id = :#{#board.id}").executeUpdate();
		//entityManger.createQuery("update Board b set b.title = board title, b.content = board content, b.author = board author  WHERE b.id = board id").executeUpdate();
		System.out.println(board);
		//String sql = "update Board b set b.title = board.title, b.content = board.content, b.author = :#{#board.author}  WHERE b.id = :#{#board.id}";
		
		String sql = "update Board b set b.title = :board.title, b.content = :board.content, b.author = :board.author WHERE b.id = :board.id";
		entityManger.createQuery(sql).executeUpdate();
	}
}
