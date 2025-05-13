# JWT API 프로젝트

Spring Boot와 JWT를 이용한 인증 기반 REST API 프로젝트입니다.

## 기술 스택

- Java
- Spring Boot 3.2.3
- Spring Security
- MyBatis
- SQLite
- JWT (JSON Web Token)
- JUnit & JaCoCo (테스트 및 커버리지)
- Swagger (API 문서화)
- Google OAuth2 (소셜 로그인)

## 개발 환경

- JDK 17
- IDE - Cursor(claude-3.7-sonnet)

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
- Swagger UI: http://localhost:8080/swagger-ui/index.html
- OAuth2 테스트 페이지: http://localhost:8080/oauth2-test

## 사용 방법

### 회원가입 및 로그인

1. 일반 회원가입

   - `/users/signup` API를 통해 회원가입
   - 필요한 정보: username, password, email, name

2. 일반 로그인

   - `/users/login` API를 통해 로그인
   - username과 password를 제공하면 JWT 토큰 발급

3. 소셜 로그인 (Google)
   - `/oauth2/login/google`로 리다이렉트하여 Google 로그인 페이지로 이동
   - 또는 테스트 페이지(http://localhost:8080/oauth2-test)에서 "Google로 로그인" 버튼 클릭
   - 로그인 성공 시 JWT 토큰 발급

### API 인증

발급받은 JWT 토큰을 HTTP 요청의 헤더에 포함시켜 인증합니다.

```
Authorization: Bearer {token}
```

### Todo 관리

1. Todo 생성

   - `/todos` API에 POST 요청으로 새 Todo 생성
   - 필요한 정보: title, content

2. Todo 조회

   - `/todos` - 모든 Todo 목록 조회
   - `/todos/{id}` - 특정 Todo 상세 조회
   - `/todos/search` - 조건에 맞는 Todo 검색

3. Todo 수정 및 삭제
   - `/todos/{id}` - PUT 요청으로 Todo 수정
   - `/todos/{id}` - DELETE 요청으로 Todo 삭제

#### SQLite 데이터베이스 초기화

데이터베이스 초기화를 위해 제공된 `init_db.sql` 스크립트를 사용할 수 있습니다. 이 스크립트는 다음과 같은 작업을 수행합니다:

- 테이블 구조 생성 (users, todos)
- 인덱스 생성
- 테스트용 샘플 데이터 추가

초기화 명령어 (Windows CMD에서 실행):

```
sqlite3 database.sqlite < init_db.sql
```

#### SQLite 데이터베이스 조회

SQLite3 명령어를 사용하여 데이터베이스를 직접 조회할 수 있습니다:

1. 테이블 목록 확인

```
sqlite3 database.sqlite ".tables"
```

2. 테이블 스키마 확인

```
sqlite3 database.sqlite ".schema users"
sqlite3 database.sqlite ".schema todos"
```

3. 데이터 조회

```
sqlite3 -header -column database.sqlite "SELECT * FROM users;"
sqlite3 -header -column database.sqlite "SELECT * FROM todos;"
```

## API 명세 요약

### 사용자 API

| 메소드 | 엔드포인트    | 설명                   | 인증 필요 |
| ------ | ------------- | ---------------------- | --------- |
| POST   | /users/signup | 회원가입               | 아니오    |
| POST   | /users/login  | 로그인 (JWT 토큰 발급) | 아니오    |
| GET    | /users/me     | 내 정보 조회           | 예        |
| PUT    | /users/me     | 내 정보 수정           | 예        |
| DELETE | /users/me     | 회원 탈퇴              | 예        |

### Todo API

| 메소드 | 엔드포인트    | 설명           | 인증 필요 |
| ------ | ------------- | -------------- | --------- |
| POST   | /todos        | Todo 생성      | 예        |
| GET    | /todos        | Todo 목록 조회 | 예        |
| GET    | /todos/{id}   | Todo 상세 조회 | 예        |
| PUT    | /todos/{id}   | Todo 수정      | 예        |
| DELETE | /todos/{id}   | Todo 삭제      | 예        |
| GET    | /todos/search | Todo 검색      | 예        |

### OAuth2 API

| 메소드 | 엔드포인트           | 설명                        | 인증 필요 |
| ------ | -------------------- | --------------------------- | --------- |
| GET    | /oauth2/login/google | Google 로그인 페이지로 이동 | 아니오    |
| GET    | /oauth2/callback     | OAuth2 인증 후 콜백 처리    | 아니오    |
| GET    | /oauth2/success      | 로그인 성공 및 토큰 반환    | 아니오    |
| GET    | /oauth2/test         | OAuth2 로그인 테스트 페이지 | 아니오    |

## 테스트 실행

```cmd
gradlew.bat test
```

## 테스트 커버리지 확인

```cmd
gradlew.bat jacocoTestReport
```

테스트 커버리지 리포트는 `build/reports/jacoco/html/index.html`에서 확인할 수 있습니다.
