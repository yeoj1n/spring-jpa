package com.spring.jpa;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.querydsl.core.types.Predicate;

@RunWith(SpringRunner.class)
@DataJpaTest
public class AccountRepositoryTest {

	@Autowired
	AccountRepository accountRepository;
	
//	// ---------- QueryDSL : 기본 repository ----------
//	
//	@Test
//	public void crud() {
//		Account account = new Account();
//		account.setFirstname("hello-user");
//		account.setLastname("kim");
//		
//		accountRepository.save(account);
//		
//		Predicate predicate = QAccount.account
//								.lastname.containsIgnoreCase("kim")
//								.and(QAccount.account.firstname.startsWith("hello"));
//		
//		Optional<Account> one = accountRepository.findOne(predicate);
//		
//		assertThat(one.get().getId()).isEqualTo(1);
//	}
	
//	// ---------- QueryDSL : 기본 repository ----------
	
}
