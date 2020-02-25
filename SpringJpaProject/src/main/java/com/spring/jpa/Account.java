package com.spring.jpa;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Entity(name = "myAccount")// hibernate 내에서 사용하는 이름
@Table(name = "Account")// table에 맵핑되는 이름
public class Account {

	@Id @GeneratedValue
	private Long id;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date created;
	
	//@Transient: column으로 맵핑하지 않고 해당 객체에서만 사용하고 싶을 때 사용
	@Transient
	private String no;

	private String username;
    
    private String password;
	
    @OneToMany(mappedBy = "owner")
    private Set<Study> studies = new HashSet<>();
    
    @Embedded
    @AttributeOverrides({
    	 @AttributeOverride(name="city", column = @Column(name="home_street"))
    })
    public Address address;
    
    public void addStudy(Study study) {
    	this.getStudies().add(study);
    	study.setOwner(this);
    }
    
    public void removeStudy(Study study) {
    	this.getStudies().remove(study);
    	study.setOwner(null);
    }

}
