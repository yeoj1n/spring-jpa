package com.spring.jpa;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;

import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;


@Component
@Transactional
public class JpaRunner implements ApplicationRunner{

	@PersistenceContext
	EntityManager entityManager;
	
	 //Entity 맵핑 확인
//	 @Override
//	 public void run(ApplicationArguments args) throws Exception {
//		 Account account = new Account();
//		 account.setUsername("kim");
//		 account.setPassword("1234");
//		 account.setCreated(new Date());
//		 
//		 Study study = new Study();
//		 study.setName("Spring JPA study");
//		 
//		 /*
//		 * account의 addStudy로 둘을 합친다.
//		 // 필수 : 관계의 주인에 mapping
//		 study.setOwner(account);
//		 // 선택적 : 객체의 관계지향적으로 생각한다면 필요하지만 꼭 적어주지않아도 동작함
//		 account.getStudies().add(study);
//		 */
//		 
//		 
//		 Session session = entityManager.unwrap(Session.class);
//		 session.save(account);
//		 session.save(study);
//		 
//		 // 이미 save한 객체이므로 select 쿼리를 실행하지 않는다.
//		 Account kim = session.load(Account.class, account.getId());
////		 System.out.println("===============");
////		 System.out.println(kim.getUsername());
//		 
//		 kim.setUsername("lee");
//		 System.out.println("===============");
//		 System.out.println(kim.getUsername());
//	}
	
	
	
	// Cascade 확인
//	@Override
//	public void run(ApplicationArguments args) throws Exception {
//		Post post = new Post();
//		post.setTitle("Spring data jpa!");
//		
//		Comment comment = new Comment();
//		comment.setComment("1빠");
//
//		//post.addComment(comment);
//		
//	
//		Session session = entityManager.unwrap(Session.class);
//		session.save(post);
//		//session.delete(post);
//	}

// --------------------------------- JPA Query ---------------------------------
	// JPQL
//	@Override
//	public void run(ApplicationArguments args) throws Exception {
//		// Post는 Table명이 아닌 Entity명
//		Query query = entityManager.createQuery("select p from Post as p", Post.class);
//		List<Post> posts = query.getResultList();
//		posts.forEach(System.out::println);
//	}
	
	// Criteria
//	@Override
//	public void run(ApplicationArguments args) throws Exception {
//		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
//		CriteriaQuery<Post> query = builder.createQuery(Post.class);
//		Root<Post> root = query.from(Post.class);
//		query.select(root);
//		
//		List<Post> posts = entityManager.createQuery(query).getResultList();
//		posts.forEach(System.out::println);
//	}
	
	// Native Query
	/*
	 * @Override public void run(ApplicationArguments args) throws Exception {
	 * List<Post> posts = entityManager.createNativeQuery("select * from post",
	 * Post.class).getResultList(); posts.forEach(System.out::println); }
	 */


//--------------------------------- JPA Query ---------------------------------


//--------------------------------- JpaRepository 사용 ---------------------------------	
//
//	@Autowired
//	PostRepository postRepository;
//	
//	@Override
//	public void run(ApplicationArguments args) throws Exception {
//		postRepository.findAll().forEach(System.out::println);
//	}
	
	@Override
	public void run(ApplicationArguments args) throws Exception {
		// TODO Auto-generated method stub
		
	}
}
