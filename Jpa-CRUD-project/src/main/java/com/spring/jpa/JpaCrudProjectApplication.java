package com.spring.jpa;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import com.spring.jpa.board.repository.SimpleMyRepository;

@SpringBootApplication
@EnableJpaRepositories(repositoryBaseClass = SimpleMyRepository.class)
public class JpaCrudProjectApplication {

	public static void main(String[] args) {
		SpringApplication.run(JpaCrudProjectApplication.class, args);
	}

}
