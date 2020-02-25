# **JPA 핵심 개념**

## **관계형 데이터베이스**
- SQL을 실행하는 비용이 비싸다.
- SQL이 db마다 다르다.
- 스키마가 변경되면 코드가 복잡해진다.
- 반복적인 코드가 많다.

=> 이러한 불편함들을 ORM으로 해결한다!
<hr/>

## **ORM 개요 (Object-Relation Mapping)**
###  **ORM** : 객체와 DB 테이블이 맵핑을 이루는 것,<br> 해당 메타데이터를 사용하여 자바 애플리케이션의 객체를 SQL DB 테이블에 자동으로 영속화 해주는 기술


ORM은 객체와 db간 캐시가 존재한다. <br> 여러 번의 쿼리를 날려도 데이터가 변해야 할 시점에 유효한 쿼리 1개만 실행된다.

<hr/>

* JDBC 사용 방법

```
try{
    Connection con = DriverManger.getConnection(url, username, password);
    String sql = "INSERT INTO Account VALUES(1,'kim','pass');
    PreparedStatement prst = con.prepareStatement(sql);
}
```

* 도메인 모델 사용 방법
```
Account account = new Account("kim", "pass");
accountRepository.save(account);
```
<br>

**도메인 모델 기반 코딩**을 하는 이유
- 객체 지향적 프로그래밍을 위해서
- 코드 재사용이 용이함
- 각종 디자인 패턴 사용이 편리하다.
- 비즈니스 로직 구현 및 테스트가 편리하다.

<br>

ORM의 장점|ORM의 단점|
---|---|
생산성|학습비용|
밴더 독립성||
성능||
유지보수성||

### **Eager-load** : SELECT 연산 시 관계있는 모든 테이블을 JOIN 하여 load 한다.
### **LAZY-load** : SELECT 연산 시 관계있는 테이블을 무시한다. 

<hr>



## **JPA 프로그래밍**

**application.properties**
```
#application이 구동될 때 마다 새로 schema를 생성(개발시 적합)
spring.jpa.hibernate.ddl-auto=create

#schema가 생성되어있다는 가정하에 객체들이 DB에 잘 맵핑되는지 검증(운영시 적합)
spring.jpa.hibernate.ddl-auto=validate

#schema를 매번 생성하지 않고 추가만 하고 싶다면 update를 사용
spring.jpa.hibernate.ddl-auto=update
#sql 출력
spring.jpa.show-sql=true
#sql을 보기 쉽게 출력하는 방법
spring.jpa.properties.hibernate.format_sql=true


#spring.jpa.hibernate.ddl-auto=create : application이 돌 때마다 스키마를 매번 새로 생성함(기존 스키마 드롭)
#spring.jpa.hibernate.ddl-auto=update : 스키마를 새로 생성하지않고 update 처리
#spring.jpa.hibernate.ddl-auto=validate : 이미 스키마가 있다는 가정하에 객체와 DB 맵핑이 잘 되는지 검증
#spring.jpa.hibernate.show-sql : console에서 sql 확인 가능
#spring.jpa.hibernate.format_sql : sql을 더 보기 좋게 정리해줌
#spring.jpa.properties.hibernate.use_sql_comment : 콘솔의 쿼리문 위에 어떤 실행을 하려는지 HINT 표시

#logging.level.org.hibernate.type.descriptor.sql : sql에 어떤 값이 들어갔는지 로그로 확인가능

#모든 옵션 적용
#spring.jpa.properties.hibernate.show_sql=true
#spring.jpa.properties.hibernate.format_sql=true
#spring.jpa.properties.hibernate.use_sql_comments=true
#logging.level.org.hibernate.type.descriptor.sql=trace
```

**Entity 맵핑 예제**
- Entity 맵핑
```
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
	
    @Column(nullable = false, unique = true)
	private String username;
	@Column
    private String password;
	
}
```
- 맵핑 확인
```
@Component
@Transactional
public class JpaRunner implements ApplicationRunner{

	@PersistenceContext
	EntityManager entityManager;
	
	 @Override
	 public void run(ApplicationArguments args) throws Exception {
		 Account account = new Account();
		 account.setUsername("kim");
		 account.setPassword("1234");
		 account.setCreated(new Date());
		 
		 entityManager.persist(account);
	}
}

```

## **@Embeddable, @Embedded**
: Entity를 하나의 column으로 이용하고 싶을 때 사용한다.

ex) Address를 Account의 column으로 이용하고 Address의 street 컬럼을 다른 컬럼명으로 변경하고 싶은 경우 

```
@Entity
public class Account {
	...

	@Embedded
	@AttributeOverrides({
		@AttributeOverride(name="street", column = @Column(name="home_street"))
	})
	public Address address;
}


@Embeddable
public class Address {
	private String city;
	private String street;
	...
}
```


**1대 다 맵핑**
: Study 와 Account 간의 관계 맵핑

1) 단방향 ManyToOne(Study에서 관리)
```
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
}


public class Study {

	@Id @GeneratedValue
	private Long id;
	
	private String name;
	
	// Study 입장: 한 사람이 많은 스터디를 만들 수 있다.
	@ManyToOne
	private Account owner;
	
}
```


- 맵핑 확인
```
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
		 study.setOwner(account);
		 
		 Session session = entityManager.unwrap(Session.class);
		 session.save(account);
		 session.save(study);
	}
}
```

study 생성 쿼리의 primary key (id) -> account의 id
```
create table study (
       id bigint not null,
        name varchar(255),
        owner_id bigint,
        primary key (id)
    ) engine=InnoDB
```
<br>
2) 단방향 OneToMany(Account에서 관리)

```
public class Study {
	...

	@OneToMany
    private Set<Study> studies = new HashSet<>();
}
public class JpaRunner implements ApplicationRunner{

		...
		 study.setOwner(account);
		 
		 account.getStudies().add(study);
		 
		 Session session = entityManager.unwrap(Session.class);
		 session.save(account);
		 session.save(study);
	}
}
```

관계에 대한 조인테이블을 생성함
```
    create table account_studies (
       my_account_id bigint not null,
        studies_id bigint not null,
        primary key (my_account_id, studies_id)
    ) engine=InnoDB
```
<br>
3) 양방향

FK를 가진 @ManyToOne 쪽이 주인이다. (주인이 아닌 @OneToMany 쪽에 mappedBy로 관계를 가진 필드를 알려주어야한다.)
<br>

```
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
		 
		 /* 필수 : 관계의 주인에 mapping */
		 study.setOwner(account);
		 /* 선택적 : 객체의 관계지향적으로 생각한다면 필요하지만 꼭 적어주지않아도 동작함 */
		 account.getStudies().add(study);
		 
		 Session session = entityManager.unwrap(Session.class);
		 session.save(account);
		 session.save(study);
	}
}
```
<br>

### **맵핑을 생각할 때는 주체가 되는 entity의 입장에서 생각한다.**

ex) Post, Comment 
```
public class Post {
	private Long id;
	private String title;

	// Post 한 개에 많은 Comment를 가짐.
	@OneToMany(mappedBy = "post")
	private Set<Comment> comments = new HashSet<>();
}

public class Comment {
	private Long id;

	private String comment;

	// 다수의 comment 들이 하나의 Post에 달린다.	
	@ManyToOne
	private Post post;
}
```

<hr/>

## **Cascade** : 엔티티 상태 변화를 전파

- Transient : JPA가 모르는 상태
- Persistent : JPA가 관리중인 상태
- Detached : JPA가 더이상 관리하지 않는 상태
- Removed : JPA가 관리하긴 하지만 삭제하기로 한 상태

### **Dirth checking / Write-behind**
Transactional write-behind(트랜잭션을 지원하는 쓰기 지연) :  트랜젝션 커밋 될때까지 내부 쿼리저장소에 모아뒀다가 한번에 실행<br>
Dirty Checking(변경감지) :
엔티티의 스냅샷을 유지하면서 엔티티의 변경사항을 체크한다. update쿼리가 항상 같음.
```
		 Account kim = session.load(Account.class, account.getId());
		 
		 kim.setUsername("lee");
		 System.out.println("===============");
		 System.out.println(kim.getUsername());
```

해당 코드 쿼리 확인:insert 후 select 없이 바로 update를 실행했다. --> **Dirth checking & Write behind**
```
Hibernate: 
    insert 
    into
        account
        (created, password, username, id) 
    values
        (?, ?, ?, ?)
		
Hibernate: 
    update
        account 
    set
        created=?,
        password=?,
        username=? 
    where
        id=?
```

<hr>

## **Fetch** : 연관 관계의 엔티티를 어떻게 가져올 것인가 (Eager, Lazy)

@OneToMany : Lazy가 기본<br>
: Post 가져올 때 Comment를 다 가져올 필요가 없다.

@ManyToOne : Eager가 기본<br>
: Comment를 가져올 때는 Post 정보가 필요하다.

<hr>

## **QUERY**

### 1. JPQL(HQL)
: 쿼리 작성 시 **Table** 기준이 아닌 **Entity** 기준
<br>
단점 : 타입 세이프 하지 않다.
```
@Override
	public void run(ApplicationArguments args) throws Exception {
		// Post는 Table명이 아닌 Entity명
		Query query = entityManager.createQuery("select p from Post as p", Post.class);
		List<Post> posts = query.getResultList();
		posts.forEach(System.out::println);
	}
```

### 2. Criteria
: 타입 세이프한 쿼리

```
@Override
	public void run(ApplicationArguments args) throws Exception {
		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Post> query = builder.createQuery(Post.class);
		Root<Post> root = query.from(Post.class);
		query.select(root);
		
		List<Post> posts = entityManager.createQuery(query).getResultList();
		posts.forEach(System.out::println);
	}
```

### 3. Native Query : 
JPQL , Criteria 처럼 query로 변경되는 방법들과 달리 직접 query를 작성하는 방법

```
@Override
	public void run(ApplicationArguments args) throws Exception {
		List<Post> posts = entityManager.createNativeQuery("select * from post", Post.class).getResultList();
		posts.forEach(System.out::println);
	}
```
