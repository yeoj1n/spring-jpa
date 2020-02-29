package com.spring.jpa;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.query.QueryLookupStrategy.Key;

@SpringBootApplication
// @EnableJpaRepositories(queryLookupStrategy = Key.CREATE)
// @EnableJpaRepositories(queryLookupStrategy = Key.USE_DECLARED_QUERY)
// @EnableJpaRepositories(queryLookupStrategy = Key.CREATE_IF_NOT_FOUND) // 기본값
public class SpringJpaProjectApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringJpaProjectApplication.class, args);
	}

}
