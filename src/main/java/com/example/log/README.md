#### 웹 애플리케이션 서버 로그 통합

- 요구사항
  - 여러 대의 서버에서 발생하는 로그를 하나의 파일로 저장

- 기능
  - 레디스에 로그 기록
  - 기록된 로그를 파일로 저장

- 구현 
  - WasLogService
    - String 형태로 log를 redis에 저장하고, 읽어와 file 에 저장하는 형태
  - WasLogListService
    - List 형태로 log를 저장하여 key-value 용량을 줄일 수 있다.