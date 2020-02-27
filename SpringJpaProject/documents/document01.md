# **16.Spring Data Common : Repository**



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

