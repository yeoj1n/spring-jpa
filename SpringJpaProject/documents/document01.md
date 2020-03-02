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
