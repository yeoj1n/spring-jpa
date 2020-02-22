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

