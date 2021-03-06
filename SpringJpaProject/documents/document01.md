# **Spring Data Common**
<hr/>

# **16.Repository**

- Spring Data Common: **PagingAndSortingRepository > CrudRepository > Repository**<br>
- Spring Data JPA : **JpaRepository**(**PagingAndSortingRepository** 상속)<br>

'>' : 상속관계를 의미 <br>

**JpaRepository, PagingAndSortingRepository, CrudRepository는 모두 @NoRepositoryBean이 선언되어있다. : Repository 가 Bean이기 때문에 중간에 있는 Repository 들이 Bean으로 중복으로 등록되는 것을 방지하기 위해서


## **JpaRepository**
Spring boot를 사용한다면 JpaRepository를 상속한 interface는 @Repository 어노테이션이 없어도 bean으로 등록된다.<br>
@EnableJpaRepositories 어노테이션은 @SpringBootApplication 어노테이션 안에 이미 등록되어있다.<br>
@SpringBootApplication 은 configuration 역할을 수행한다.


만약 Spring을 사용한다면 @Configuration이 있는 부분에 @EnableJpaRepositories 어노테이션을 추가해야 bean으로 등록된다.

- Spring boot 사용시
```
@SpringBootApplication
public class SpringJpaProjectApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringJpaProjectApplication.class, args);
	}

}


public interface PostRepository extends JpaRepository<Post, Long> {	
}
```

- Spring 사용시 
```
@Configuration
@EnableJpaRepositories
public class SpringJpaProjectApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringJpaProjectApplication.class, args);
	}

}


public interface PostRepository extends JpaRepository<Post, Long> {	
}
```

## **JpaRepository 사용해보기**

### Entity class

```
@NoArgsConstructor
@Getter
@ToString
@Entity
public class Post {

	@Id @GeneratedValue
	private Long id;
	
	private String title;
	
	@OneToMany(mappedBy = "post", cascade = CascadeType.PERSIST)
	private Set<Comment> comments = new HashSet<>();
	
	public void addComment(Comment comment) {
		this.getComments().add(comment);
		comment.setPost(this);
	}
	
	public void removeComment(Comment comment) {
		this.getComments().remove(comment);
		comment.setPost(null);
	}
	
	@Builder
	public Post(Long id, String title, Set<Comment> comments) {
		this.id = id;
		this.title = title;
		this.comments = comments;
	}
}

@NoArgsConstructor @AllArgsConstructor
@Getter @Setter
@ToString
@Entity
public class Comment {

	@Id @GeneratedValue
	private Long id;
	
	private String comment;
	
	@ManyToOne
	private Post post;
}
```

### Repository 테스트(junit 사용)
```
@RunWith(SpringRunner.class)
@DataJpaTest
public class PostRepositoryTest {

	@Autowired
	PostRepository postRepository;
	
	@Test
	@Rollback(false)
	public void crudRepositoy(){
		// Given
		Post post = new Post();
		post.setTitle("hello spring boot common!");
		assertThat(post.getId()).isNull();
		
		// When
		Post newPost = postRepository.save(post);
		List<Post> posts = postRepository.findAll();
		
		//Then
		assertThat(posts.size()).isEqualTo(1);
		assertThat(posts.contains(newPost));
	
		// When
		Page<Post> page = postRepository.findAll(PageRequest.of(0, 10));
		
		//Then
		assertThat(page.getTotalElements()).isEqualTo(1);
		assertThat(page.getNumber()).isEqualTo(0);// page는 0부터 시작
		assertThat(page.getTotalPages()).isEqualTo(1);
		assertThat(page.getSize()).isEqualTo(10);
		assertThat(page.getNumberOfElements()).isEqualTo(1);
		
		//When
		page = postRepository.findByTitleContains("spring",PageRequest.of(0, 10));
		
		//Then
		assertThat(page.getTotalElements()).isEqualTo(1);
		assertThat(page.getNumber()).isEqualTo(0);// page는 0부터 시작
		assertThat(page.getTotalPages()).isEqualTo(1);
		assertThat(page.getSize()).isEqualTo(10);
		assertThat(page.getNumberOfElements()).isEqualTo(1);
		
		//When
		long count = postRepository.countByTitleContains("spring");
		
		//Then
		assertThat(count).isEqualTo(1);
	}
}

```


<hr>

# **17.Repository interface 정의**
<br>

### **1) Repository 인터페이스로 직접 메소드를 정의하는 방법**<br>
: @RepositoryDefinition 어노테이션을 추가하고 사용할 메소드를 정의한다.

**Repository 정의**
```
@RepositoryDefinition(domainClass=Comment.class, idClass=Long.class)
public interface CommentRepository {
	
	Comment save(Comment comment);
	List<Comment> findAll();
    Long count();
}

```


### **2) 공통 인터페이스 정의**<br>
: @NoRepositoryBean을 사용하여 공통 인터페이스를 정의 후 사용한다.

**공통 repository 정의 및 사용할 interface에서 상속받기**
```
@NoRepositoryBean
public interface CommonRepository<T, ID extends Serializable> extends Repository<T, ID>{
	//Repository : 아무 기능이 없는 마크 인터페이스
	
	<E extends T>E save(E entity);
	
	List<T> findAll();
	
	Long count();

}

// 공통 인터페이스 상속
public interface CommentRepository extends CommonRepository<Comment, Long>{
}
```

### **1. , 2. test**
```
@RunWith(SpringRunner.class)
@DataJpaTest
public class CommentRepositoryTest {

	@Autowired
	CommentRepository commentRepository;
	

	@Test
	public void crud() {
		Comment comment = new Comment();
		comment.setComment("this is comment");
		
		commentRepository.save(comment);
		
		// When
		List<Comment> comments = commentRepository.findAll();

		// Then
		assertThat(comments.size()).isEqualTo(1);
		
		// When
		Long count = commentRepository.count();
		
		// Then
		assertThat(count).isEqualTo(1);
	}	
}
```

# **18.Null 처리하기**
<br>
### **1. Optional** 을 이용한 null처리

: Optional은 Null을 리턴하지 않고 Optional.empty을 리턴한다.
<br>


**Optional 사용예제**

```
Optional<Comment> byId = commentRepository.findById(100L);

byId.isPresent();
byId.orElse(null);
byId.orElseThrow(() -> new NoSuchElementException()); // 예외처리
```

### **2. Collection** 을 이용한 null처리
: Collection은 Null을 리턴하지 않고 빈 콜렉션을 리턴한다.

**Collection 사용예제**

```
List<Comment> comments = commentRepository.findAll();

if(comments != null) // 올바르지 않은 방법
assertThat(comments).isEmpty();
```

# **19/20. 쿼리 만들기**
- 메소드 이름을 분석해서 쿼리를 만드는 방법(Spring data 사용 find, contains 등)
- USE_DECLARED_QUERY: 미리 정의해둔 쿼리를 사용(@Query를 찾아 적용)
- CREATE_IF_NOT_FOUND(기본 값) : 선언된 쿼리 @Query 가 없으면 메소드 이름을 분석해서 쿼리를 만든다.

```
@SpringBootApplication
@EnableJpaRepositories(queryLookupStrategy = Key.CREATE)
@EnableJpaRepositories(queryLookupStrategy = Key.USE_DECLARED_QUERY)
@EnableJpaRepositories(queryLookupStrategy = Key.CREATE_IF_NOT_FOUND) // 기본값
public class SpringJpaProjectApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringJpaProjectApplication.class, args);
	}
}

public interface CommentRepository extends CommonRepository<Comment, Long> {
	
	@Query(value = "SELECT c FROM comment AS c", nativeQuery = true)
	List<Comment> findByTitleContains(String keyword);
	
	
}
```
### **쿼리만드는 규칙** <br>
: 리턴타입 {접두어}{도입부}By{프로퍼티 표현식}(조건식)[(And|Or){프로퍼티표현식}(조건식)]{정렬조건}(매개변수)


| | 메소드
|------|---|
|접두어|  Find, Get, Query, Count ...
|도입부| Distinct, Fist(N), Top(N)
|프로퍼티 표현식| Persion, Address, ZipCode => find(Person)ByAddress_ZipCode(...)
|조건식| IgnoreCase, Between, LessThan, GreaterThan, Like, Contains, ...
|정렬 조건| OrderBy{프로퍼티}Asc|Desc
|리턴 타입| E, Optional<E>, List<E>, Page<E>, Slice<E>, Stream<E>
|매개 변수| Pageable, Sort

### **예제**

```
@Entity
public class Comment {

	@Id @GeneratedValue
	private Long id;
	
	private String comment;
	
	private Integer likeCount;
	
	@ManyToOne
	private Post post;
}

public interface CommentRepository extends CommonRepository<Comment, Long> {
	
	@Query(value = "SELECT c FROM comment AS c", nativeQuery = true)
	List<Comment> findByCommentContains(String keyword);
	
	Page<Comment> findByLikeCountGreaterThan(int likeCount, Pageable page);
	
	List<Comment> findByCommentContainsIgnoreCaseAndLikeCountGreaterThan(String keyword, int likeCount);
}

@RunWith(SpringRunner.class)
@DataJpaTest
public class CommentRepositoryTest {

	@Autowired
	CommentRepository commentRepository;
	

	@Test
	public void crud() {

		Comment comment1 = new Comment();
		comment1.setComment("test1");
		comment1.setLikeCount(2);
		
		Comment comment2 = new Comment();
		comment2.setComment("test2");
		comment2.setLikeCount(10);
		
		commentRepository.save(comment1);
		commentRepository.save(comment2);

		Page<Comment> pages = commentRepository.findByLikeCountGreaterThan(2, PageRequest.of(0, 10));
		
		pages.forEach(c -> System.out.println(c.getComment()));
		// ----------------- 쿼리 만들기 -----------------
		
		// ----------------- 쿼리 만들기 실습-----------------
		
		//List<Comment> comments = commentRepository.findByCommentContainsIgnoreCaseAndLikeCountGreaterThan("test", 1);
		//assertThat(comments.size()).isEqualTo(2);
		
		
		try(Stream<Comment> comments = commentRepository.findByCommentContainsIgnoreCaseAndLikeCountGreaterThan("test", 1)) {
			Comment firstComment = comments.findFirst().get();
			//assertThat(firstComment.getComment()).isEqualTo("test1");
			System.out.println(firstComment.getComment());
		}
	}
}
```

# **21. 비동기 쿼리**


# **22. 커스텀 레포지토리**

쿼리 메소드로 해결이 되지않는 경우 직접 코딩으로 구현한다.<br>

### **1. Spring Data Repository 인터페이스에 기능 추가**

### **2. Spring Data Repository 기본 기능 덮어쓰기**
<br>

### **구현예제**
```
public interface PostCustomRepository<T> {

	List<Post> findMyPost(); // 기능 추가

	void delete(T entity); // 기능 덮어쓰기
}


@Repository
@Transactional
public class PostCustomRepositoryImpl implements PostCustomRepository<Post>{

	@Autowired
	EntityManager entityManager;
	
	@Override
	public List<Post> findMyPost() {
		return entityManager.createQuery("SELECT p FROM Post AS p", Post.class).getResultList();
	}

	// detached : 한번 persitent 상태였다가 빠져나온 상태, transaction에서 빠져나온 상태로 더이상 persistent 상태가 아님
	@Override
	public void delete(Post post) {
		entityManager.detach(post);// 제거
	}
}


public interface PostRepository extends JpaRepository<Post, Long>, PostCustomRepository<Post>{
}


public class PostRepositoryTest {

	@Autowired
	PostRepository postRepository;

	@Test
	@Rollback(false)
	public void crudRepositoy(){
		//postRepository.findMyPost();
		
		Post post = new Post();
		post.setTitle("hibernate");
		postRepository.save(post);
		
		postRepository.delete(post);
		// postRepository.delete만 하면 rollaback 때문에 delete 하지않음, delete 를 하고 싶다면 flush 추가
		postRepository.flush();
		
		// insert, delete 쿼리 날아가지 않는 이유 : 마지막까지 검토(?) 후 필요한 쿼리만 실행하기 때문
	}
}
```

### **3. 접미어 설정하기**
Custom Repository의 기본 접미사는 Impl이다.(PostCustomRepositoryImpl)
변경하려면 @EnableJpaRepositories 어노테이션의 repositoryImplementationPostfix 를 이용하여 정의

### **구현예제**

```
@SpringBootApplication
@EnableJpaRepositories(repositoryImplementationPostfix = "Default")
public class SpringJpaProjectApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringJpaProjectApplication.class, args);
	}
}


@Repository
@Transactional
public class PostCustomRepositoryDefault implements PostCustomRepository<Post>{

	@Autowired
	EntityManager entityManager;
	
	@Override
	public List<Post> findMyPost() {
		return entityManager.createQuery("SELECT p FROM Post AS p", Post.class).getResultList();
	}

	// Detached 상태 : 한번 persitent 상태였다가 빠져나온 상태, transaction에서 빠져나온 상태로 더이상 persistent 상태가 아님
	@Override
	public void delete(Post post) {
		entityManager.detach(post);// 제거
	}
}
```

# **23. 기본 레포지토리 커스터마이징**

모든 레포지토리에 공통적으로 추가하고 싶은 기능이나 덮어쓰고 싶은 기능이 있을 때

### **1. JpaRepository 를 상속받는 repository 생성**
중복으로 bean이 등록되는 것을 막기 위해 @NoRepositoryBean 어노테이션을 추가해준다.

```
// 모든 repository에 공통으로 적용할 repository
// JpaRepository를 상속받을 것이기 때문에 추가되는 repository이므로 중복 Bean등록을 막기위해 @NoRepositoryBean 필수
@NoRepositoryBean
public interface MyRepository<T, ID extends Serializable> extends JpaRepository<T, ID> {
	
	// entity가 Persistent context에 들어있는지 확인하는 기능
	boolean contains(T entity);
}
```

### **2. 커스텀 구현**
SimpleJpaRepository를 상속받는 구현체 생성

```
public class SimpleMyRepository<T, ID extends Serializable> extends SimpleJpaRepository<T, ID> implements MyRepository<T, ID>{

	// Bean으로 만들어줄 필요가 없고 SimpleMyRepositoryImpl로 전달해주는 역할만 하면 된다.
	private EntityManager entityManager;
	
	// 생성자 주입
	private SimpleMyRepository(JpaEntityInformation<T, ?> entityInformation, EntityManager entityManager) {
		super(entityInformation, entityManager);
		this.entityManager = entityManager;
	}

	@Override
	public boolean contains(T entity) {
		return entityManager.contains(entity);
	}
}
```

### **3. @EnableJpaRepositories 에 repositoryBaseClass를 지정**
커스텀한 인터페이스를 상속받은 모든 repository에서 커스텀 메소드를 사용할 수 있다.
```
public interface PostRepository extends MyRepository<Post, Long> {
	
}

@SpringBootApplication
@EnableJpaRepositories(repositoryBaseClass = SimpleMyRepository.class)
public class SpringJpaProjectApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringJpaProjectApplication.class, args);
	}

}
```


### ** 커스텀 repository 사용예제**

```
public interface PostRepository extends MyRepository<Post, Long> {
	
}

@Test
	public void crud() {
		Post post = new Post();
		post.setTitle("post 1");
		
		// 저장 전이므로 post 객체의 상태는 Transient(JPA가 모르는) 상태
		assertThat(postRepository.contains(post)).isFalse();
		
		postRepository.save(post);
		
		// 저장 후므로 post 객체의 상태는 Persist(JPA가 관리중인) 상태
		assertThat(postRepository.contains(post)).isTrue();
	
		
		postRepository.delete(post);
		postRepository.flush(); 
		//flush 하면  삭제 후이므로 post 상태는 Detach(JPA가 관리하지 않는) 상태
	}
```

# **24. 도메인 이벤트**

이벤트를 발생시키는 방법은 2가지

사용할 **Entity** 와 **Repsitory**

**Entity**
```
@NoArgsConstructor @AllArgsConstructor
@Getter @Setter
@ToString (exclude = "comments")
@Entity
public class Post {

	@Id @GeneratedValue
	private Long id;
	
	private String title;
	
	@OneToMany(mappedBy = "post", cascade = CascadeType.PERSIST)
	private Set<Comment> comments = new HashSet<>();
	
	public void addComment(Comment comment) {
		this.getComments().add(comment);
		comment.setPost(this);
	}
	
	public void removeComment(Comment comment) {
		this.getComments().remove(comment);
		comment.setComment(null);
	}

}

```

**Repository**
```
public interface PostRepository extends JpaRepository<Post, Long> {
	
}
```

**이벤트와 이벤트 리스너 생성**

1. 이벤트 class 생성

	```
	// Post 를 발행하는 event 클래스
	public class PostPublishedEvent extends ApplicationEvent{

		@Getter
		private final Post post;
		
		public PostPublishedEvent(Object source) {
			super(source);
			this.post = (Post) source;
		}
	}
	```
2. 이벤트 리스너 생성

	```
	// @EventListener 사용하는 방법
	public class PostListener {
		
		@EventListener
		public void onApplicationEvent(PostPublishedEvent event) {
			System.out.println("-------------------------------");
			System.out.println(event.getPost() + "is published!");
			System.out.println("-------------------------------");
		}
	}


	// ApplicationListener 상속방는 방법
	// 이벤트 listener class (PostRepositoryTestConfig.class에서 bean으로 등록)
	public class PostListener implements ApplicationListener<PostPublishedEvent>{
		
		// 이벤트 발생 시 해야할 일
		@Override
		public void onApplicationEvent(PostPublishedEvent event) {
			System.out.println("-------------------------------");
			System.out.println(event.getPost() + "is published!");
			System.out.println("-------------------------------");
		}

	}

	```

3. listener bean으로 등록 

	```
	@Configuration
	public class PostRepositoryTestConfig {

		@Bean
		public PostListener postListener() {
			return new PostListener();
		}
	}
	```

**1. 스프링 프레임워크의 이벤트 기능 사용(ApplicationEventPublisher)**


	```
	@RunWith(SpringRunner.class)
	@DataJpaTest
	@Import(PostRepositoryTestConfig.class) // 이벤트 리스너 bean 을 주입
	public class PostRepositoryTest {

		@Autowired
		PostRepository postRepository;
		
		@Test
		public void event() {
			Post post = new Post();
			post.setTitle("event");
			
			PostPublishedEvent event = new PostPublishedEvent(post);
			applicationContext.publishEvent(event); // 이벤트 발행
			
		}
	}
	```


**2. 스프링 데이터의 도메인 이벤트 기능 사용(AbstractAggregateRoot)**

1. 도메인에 AbstractAggregateRoot 상속, 이벤트 발생 메소드 생성

	```
	public class Post extends AbstractAggregateRoot<Post>{
		...

			
		// save 시 publish 해주면 이벤트를 발생시킨다.
		public Post publish() {
			// 이벤트 발행
			this.registerEvent(new PostPublishedEvent(this));
			return this;
		}
		
	}
	```

2. test

	```
	@RunWith(SpringRunner.class)
	@DataJpaTest
	@Import(PostRepositoryTestConfig.class) // 이벤트 리스너 Bean 을 주입
	public class PostRepositoryTest {

		@Autowired
		PostRepository postRepository;
		
		@Test
		public void event() {
			Post post = new Post();
			post.setTitle("event");
			
			// 이벤트 발행
			postRepository.save(post.publish());
		}
	}
	```


**결과 값**

	-------------------------------
	Post(id=1, title=event)is published!
	-------------------------------


# **25. QueryDSL**

-> 타입 세이프한 쿼리를 만들 수 있게 도와주는 라이브러리

-> 쿼리를 java 코드로 만들수 있음

## **QueryDSL 사용방법**

1. queryDSL 의존성 주입 및 plugin 설정

```
	<dependency>
		<groupId>com.querydsl</groupId>
		<artifactId>querydsl-jpa</artifactId>
		<version>4.2.2</version>
	</dependency>
	
	<dependency>
		<groupId>com.querydsl</groupId>
		<artifactId>querydsl-apt</artifactId>
		<version>4.2.2</version>
	</dependency>

	<build>
		<plugins>
			...
			
			<plugin>
                <groupId>com.mysema.maven</groupId>
                <artifactId>apt-maven-plugin</artifactId>
                <version>1.1.3</version>
                <executions>
                    <execution>
                        <goals>
                            <goal>process</goal>
                        </goals>
                        <configuration>
                            <outputDirectory>${project.build.directory}/generated-sources/java/</outputDirectory>
         					<processor>com.querydsl.apt.jpa.JPAAnnotationProcessor</processor>
                        </configuration>
                    </execution>
                </executions>
            </plugin>
		</plugins>
	</build>
```

2. maven/ gradle compile

3. compile이 success되면 다음과 같이 target 하위에 queryDSL 파일들이 생성됨.
![target](./images/25-1.PNG)

4. 기본 Repository 에 QuerydslPredicateExecutor 상속

```
public interface AccountRepository extends JpaRepository<Account, Long>, QuerydslPredicateExecutor<Account> {

}
```

5. Repository Test

```
@RunWith(SpringRunner.class)
@DataJpaTest
public class AccountRepositoryTest {

	@Autowired
	AccountRepository accountRepository;
	
	@Test
	public void crud() {
		Account account = new Account();
		account.setFirstname("hello-user");
		account.setLastname("kim");
		
		accountRepository.save(account);
		
		Predicate predicate = QAccount.account
								.lastname.containsIgnoreCase("kim")
								.and(QAccount.account.firstname.startsWith("hello"));
		
		Optional<Account> one = accountRepository.findOne(predicate);
		
		assertThat(one.get().getId()).isEqualTo(1);
	}
}
```

**강의에서는 기본 리포지토리를 커스터마이징 한 경우 QuerydslPredicateExecutor 를 상속받아 사용할 수 없다고 하는데 테스트해보니 QuerydslPredicateExecutor 상속받으면 바로 사용이 가능하다.**

```
public interface PostRepository extends MyRepository<Post, Long>, QuerydslPredicateExecutor<Post> {
	
}



@RunWith(SpringRunner.class)
@DataJpaTest
@Import(PostRepositoryTestConfig.class) // 이벤트 리스너 bean 을 주입
public class PostRepositoryTest {

	@Autowired
	PostRepository postRepository;

	@Test
		public void crud() {
			Comment comment1 = new Comment();
			comment1.setComment("test comment1!");
			comment1.setLikeCount(5);
			
			Comment comment2 = new Comment();
			comment2.setComment("test comment2!");
			comment2.setLikeCount(3);
			
			commentRepository.save(comment1);
			commentRepository.save(comment2);
			
			
			Set<Comment> set = new HashSet<>();
			set.add(comment1);
			set.add(comment2);
			
			Post post = new Post();
			post.setTitle("hibernate!");
			post.setComments(set);
			
			postRepository.saveAndFlush(post.publish());
			
			Predicate predicate = QPost.post.title.contains("hi");
			Optional<Post> one = postRepository.findOne(predicate);
			assertThat(one).isNotEmpty();
	}
}
```

# **26. Web : 웹 지원 기능 소개**
- DomainClassConverter
- Page 관련 HATEOAS 기능
- Payload 프로젝션

# **27. Web : DomainClassConverter**
: 요청 값을 도메인 플래스로 변환하여 controller의 method에 전달하는 Converter

**예제**
```
	// 일반적인 사용방법
	@GetMapping("/{id}")
	public Board getBoard(@PathVariable("id")long id) {
		return boardRepository.findById(id).get();
	}

	// DomainClassConverter 사용
	@GetMapping("/{id}")
	public Board getBoard(@PathVariable("id")Board board) {
		return board;
	}
```

# **28. Web : Pageable & sort**
: Pageable 사용시 sorting도 할 수 있다.

### ** 페이징과 정렬 관련 매개변수**
-> page : 0부터 시작
-> size : 20 (기본값)
-> sort : property.property(.ASC(기본값)|DESC)
ex) sort=created.desc&sort=title



**예제**
```
@RestController
@RequestMapping("/board")
public class BoardController {	

	// 10개씩 페이징
	@GetMapping("/list/{pageNum}")
	public Page<Board> getBoardList(@PathVariable("pageNum")int pageNum) {
		Page<Board> boardList = boardRepository.findAll(PageRequest.of(pageNum, 10));
		return boardList;
	}
}


// Test(paging & sorting)

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class BoardControllerTest {

	@Autowired
	MockMvc mockMvc;
	
	@Autowired
	BoardRepository boardRepository;
	
	@Test
	public void getBoards() throws Exception {
		Board board1 = Board.builder()
				.title("title1")
				.content("board content1")
				.author("test user")
				.build();
		
		Board board2 = Board.builder()
				.title("title2")
				.content("board content2")
				.author("test user")
				.build();
		
		boardRepository.save(board1);
		boardRepository.save(board2);
		
		mockMvc.perform(get("/board/list")
						.param("page", "0")
						.param("size", "10")
						.param("sort", "createdAt,desc")
						.param("sort", "title"))
						.andDo(print())
						.andExpect(status().isOk())
						.andExpect(jsonPath("$.content[0].title", is("title2")));
	}
	
}
```

# **29. Web : HATEOAS**

**예제**

```
@RestController
@RequestMapping("/board")
public class BoardController {	

	// 2.2.4 기준 : PagedModel<EntityModel<Entity>> / 이전 기준 PagedResources<Resource<Entity>>
	@GetMapping("/list")
	public PagedModel<EntityModel<Board>> getBoardList(Pageable pageable, PagedResourcesAssembler<Board> assembler) {
		return assembler.toModel(boardRepository.findAll(pageable));
	}
}


// Test(paging & sorting)

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class BoardControllerTest {

	@Autowired
	MockMvc mockMvc;
	
	@Autowired
	BoardRepository boardRepository;
	
	@Test
	public void getBoards() throws Exception {
		Board board1 = Board.builder()
				.title("title1")
				.content("board content1")
				.author("test user")
				.build();
		
		Board board2 = Board.builder()
				.title("title2")
				.content("board content2")
				.author("test user")
				.build();
		
		boardRepository.save(board1);
		boardRepository.save(board2);
		
		mockMvc.perform(get("/board/list")
						.param("page", "0")
						.param("size", "10")
						.param("sort", "createdAt,desc")
						.param("sort", "title"))
						.andDo(print())
						.andExpect(status().isOk());
	}
	
}
```

**응답 값 비교**

리소스로 변환 전 (HATEOAS 사용 x)

```
{
  "content": [
    {
      "id": 2,
      "author": "test user",
      "title": "title2",
      "content": "board content2",
      "createdAt": "2020-03-09T18:42:03.876"
    },
    {
      "id": 1,
      "author": "test user",
      "title": "title1",
      "content": "board content1",
      "createdAt": "2020-03-09T18:42:03.835"
    }
  ],
  "pageable": {
    "sort": {
      "sorted": true,
      "unsorted": false,
      "empty": false
    },
    "offset": 0,
    "pageSize": 10,
    "pageNumber": 0,
    "unpaged": false,
    "paged": true
  },
  "totalPages": 1,
  "last": true,
  "totalElements": 2,
  "number": 0,
  "size": 10,
  "numberOfElements": 2,
  "sort": {
    "sorted": true,
    "unsorted": false,
    "empty": false
  },
  "first": true,
  "empty": false
}
```


리소스로 변환 후 (HATEOAS 사용)

```
{
  "_embedded": {
    "boardList": [
      {
        "id": 2,
        "author": "test user",
        "title": "title2",
        "content": "board content2",
        "createdAt": "2020-03-09T18:42:55.467"
      },
      {
        "id": 1,
        "author": "test user",
        "title": "title1",
        "content": "board content1",
        "createdAt": "2020-03-09T18:42:55.433"
      }
    ]
  },
  "_links": {
    "self": {
      "href": "http://localhost/board/list?page=0&size=10&sort=createdAt,desc&sort=title,asc"

	  // sorting 없는 경우 다음과 같이 출력
	  //"href": "http://localhost/board/list?page=0&size=10"
    }
  },
  "page": {
    "size": 10,
    "totalElements": 2,
    "totalPages": 1,
    "number": 0
  }
}

```