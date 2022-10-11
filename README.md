#### Redis - Spring 연동

#### 예제

- com.example.redis.log
  - Redis Template 를 활용하여 log 성 데이터를 파일로 저장하는 client
  - String type / List type 사용

- com.example.redis.pageCount
  - Redis Template 활용하여 페이지 접속 시 total/pageId key 에 대한 increment 및 조회 기능
  - String type / hash type 사용

- com.example.redis.cart
  - Repository 를 사용하여 Item, Member, Cart 를 저장

- com.example.redis.viewCount
  - Redis Template 사용
  - bit 연산으로 페이지 ID - 회원 ID 방문했는 지 확인

- com.example.redis.item
  - Redis 캐시로 사용

#### 개념 정리

- Redis 기본
  - https://devhtak.github.io/no%20sql/2022/08/20/Redis_Basic.html
- Redis 와 Spring
  - https://devhtak.github.io/no%20sql/2022/08/27/Redis_Spring.html

#### 예제 출처

- 책 이것이 레디스다 7장을 바탕으로 예제 구성
