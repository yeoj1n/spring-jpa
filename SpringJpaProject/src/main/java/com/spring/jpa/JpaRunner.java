package com.spring.jpa;

import java.util.Date;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.hibernate.Session;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;


@Component
@Transactional
public class JpaRunner implements ApplicationRunner{

	@PersistenceContext
	EntityManager entityManager;
	
	 /* Entity 맵핑 확인 */
	 @Override
	 public void run(ApplicationArguments args) throws Exception {
		 Account account = new Account();
		 account.setUsername("kim");
		 account.setPassword("1234");
		 account.setCreated(new Date());
		 
		 Study study = new Study();
		 study.setName("Spring JPA study");
		 
		 /*
		 * account의 addStudy로 둘을 합친다.
		 // 필수 : 관계의 주인에 mapping
		 study.setOwner(account);
		 // 선택적 : 객체의 관계지향적으로 생각한다면 필요하지만 꼭 적어주지않아도 동작함
		 account.getStudies().add(study);
		 */
		 
		 
		 Session session = entityManager.unwrap(Session.class);
		 session.save(account);
		 session.save(study);
	}
}
