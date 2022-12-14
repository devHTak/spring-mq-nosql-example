#### 페이지 방문 횟수 저장

- 요구사항
  - 등록된 이벤트 페이지에 대하여 사용자가 방문한 횟수 조회
  - 이벤트 정보는 실시간으로 추가되고 삭제되지만, 이벤트가 종료되어도 해당 이벤트 페이지의 방문 횟수는 유지되어야 한다

- 기능 정의
  - 이벤트 페이지 방문 횟수 저장
  - 모든 페이지의 방문 횟수 조회
  - 개별 이벤트 페이지 방문 횟수 조회

- 기능 구현
  - 레디스에서 숫자를 저장하고 증가시키기 위해서는 문자열 데이터 또는 해시 데이터 형을 사용할 수 있다.
  - 키 설계(event:click:{eventId}, event:click:total)

- 기능 확장
  - 이벤트 이름을 클릭하면, 새로 보여지는 화면에서 날짜별 방문 횟수와 날짜별 전체 이벤트 페이지 방문 횟수 출력해야 한다
  - 키 설계(event:click:daily:{date}:{eventId}, event:click:daily:total:{date})

- 기능 확장 시 문자열 데이터를 사용하는 경우 문제점이 있다
  - 이벤트 시작일, 종료일 등을 확인할 수 없다
  - RDBMS 에 저장하여 활용할 수 있지만, 해시 데이터에 저장하여 사용할 수 있다.
  - 키 설계(event:click:total:hash, event:click:hash:{eventId)