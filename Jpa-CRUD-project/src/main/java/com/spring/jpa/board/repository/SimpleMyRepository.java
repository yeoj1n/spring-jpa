package com.spring.jpa.board.repository;

import java.io.Serializable;

import javax.persistence.EntityManager;

import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;

import com.sun.el.stream.Optional;

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
}
