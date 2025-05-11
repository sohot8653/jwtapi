# JWT API 프로젝트

Spring Boot와 JWT를 이용한 인증 기반 REST API 프로젝트입니다.

## 기술 스택

- Java
- Spring Boot 3.4.5
- Spring Security
- MyBatis
- SQLite
- JWT (JSON Web Token)
- JUnit & JaCoCo (테스트 및 커버리지)
- Swagger (API 문서화)

## 실행 방법

### 요구사항

- JDK 17 이상
- Git

### 설치 및 실행

1. 프로젝트 클론

```cmd
git clone https://github.com/sohot8653/jwtapi.git
cd jwtapi
```

2. 프로젝트 빌드

```cmd
gradlew.bat build
```

3. 애플리케이션 실행

```cmd
gradlew.bat bootRun
```

4. 애플리케이션 접속

- API 서버: http://localhost:8080
- Swagger UI: http://localhost:8080/swagger-ui.html

## API 명세 요약

### 사용자 API

| 메소드 | 엔드포인트     | 설명                   | 인증 필요 |
|--------|----------------|------------------------|-----------|
| POST   | /users/signup  | 회원가입               | 아니오    |
| POST   | /users/login   | 로그인 (JWT 토큰 발급) | 아니오    |
| GET    | /users/me      | 내 정보 조회           | 예        |
| PUT    | /users/me      | 내 정보 수정           | 예        |
| DELETE | /users/me      | 회원 탈퇴              | 예        |

### Todo API

| 메소드 | 엔드포인트        | 설명            | 인증 필요 |
|--------|--------------------|-----------------|-----------|
| POST   | /todos             | Todo 생성       | 예        |
| GET    | /todos             | Todo 목록 조회  | 예        |
| GET    | /todos/{id}        | Todo 상세 조회  | 예        |
| PUT    | /todos/{id}        | Todo 수정       | 예        |
| DELETE | /todos/{id}        | Todo 삭제       | 예        |
| GET    | /todos/search      | Todo 검색       | 예        |

## 인증 방식

JWT 토큰 기반 인증을 사용합니다.

1. `/users/login` API를 통해 인증 후 JWT 토큰 발급
2. 발급받은 토큰을 API 요청 시 Authorization 헤더에 포함
   ```
   Authorization: Bearer {token}
   ```

## 테스트 실행

```cmd
gradlew.bat test
```

## 테스트 커버리지 확인

```cmd
gradlew.bat jacocoTestReport
```

테스트 커버리지 리포트는 `build\reports\jacoco\html\index.html`에서 확인할 수 있습니다. 