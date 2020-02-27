package com.spring.jpa;

import javax.persistence.Embeddable;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
	

@NoArgsConstructor @AllArgsConstructor
@Embeddable
public class Address {

	private String street;
	
	private String city;
	
	private String state;
	
	private String zipCode; 
	
}
