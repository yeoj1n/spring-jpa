package com.spring.jpa;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
// @EnableJpaRepositories(queryLookupStrategy = Key.CREATE)
// @EnableJpaRepositories(queryLookupStrategy = Key.USE_DECLARED_QUERY)
// @EnableJpaRepositories(queryLookupStrategy = Key.CREATE_IF_NOT_FOUND) // 기본값

/*
	기본적으로 커스텀 레포지토리의 접미어는 Impl 이다. 변경하고 싶은 경우 repositoryImplementationPostfix로 정의한다.
	예를 들어 repositoryImplementationPostfix = "Default" 로 정의한다면 
	PostCustomRepositoryImpl 가 아닌 PostCustomRepositoryDefault 로 class를 정의해야한다.
*/
//@EnableJpaRepositories(repositoryImplementationPostfix = "Default")
@EnableJpaRepositories(repositoryBaseClass = SimpleMyRepository.class)
public class SpringJpaProjectApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringJpaProjectApplication.class, args);
	}

}
