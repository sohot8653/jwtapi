# AI 프롬프트 사용 내역

---

## 1. SQLite3 JDBC 드라이버 및 Mybatis 설정을 해줘

**프롬프트:**

> SQLite3 JDBC 드라이버 및 Mybatis 설정을 해줘

**응답 요약:**

- `build.gradle`에 SQLite JDBC 및 MyBatis 의존성 추가
- `application.yml` 또는 `application.properties`에 데이터베이스 설정 추가
- MyBatis 설정 파일 (`mybatis-config.xml`) 및 매퍼 스캔 설정 제공

---

## 2. MyBatis 관련된 의존성을 못 가지고 오고 있는데 확인해줘

**프롬프트:**

> mybatis 관련된 의존성을 못가지고 오고있는데 확인해줘.

**응답 요약:**

- 의존성 충돌 가능성 점검
- Gradle 리포지토리 설정 (`mavenCentral()`) 확인
- 의존성 버전과 정확한 groupId, artifactId 확인

---

## 3. API 구현 요청

**프롬프트:**

> 이제 API를 구현해야해. 아래의 요건을 기반으로 API들을 작성해줘.  
> 사용자 관련과 TODO 관련은 각각 Controller, Service 로 파일을 구분해주고 MyBatis단 쿼리까지 작성해줘.  
> 해당 어플리케이션에서는 VO와 DTO를 사용할 건데, VO로 파라미터를 받고 ModelMapper를 활용해서 DTO로 객체를 복사해서 Service단의 비즈니스 로직에서 사용할 수 있도록 구성해줘.

**요구사항:**

- 사용자 API: 회원가입, 로그인, 내 정보 조회/수정/삭제, 소셜 로그인(옵션)
- TODO API: 생성, 전체 조회, 단건 조회, 수정, 삭제, 검색
- 구조: Controller → Service → Mapper
- VO → DTO 매핑 (ModelMapper 사용)

**응답 요약:**

- `UserController`, `UserService`, `TodoController`, `TodoService` 구조 예시 작성
- VO, DTO, Mapper 및 SQL 쿼리 예시 포함
- ModelMapper 설정 예시 포함

---

## 4. Swagger 적용 요청

**프롬프트:**

> 만들어진 API 전체에 Swagger를 적용시켜줘.

**응답 요약:**

- Springfox 또는 springdoc-openapi 의존성 추가
- Swagger UI 설정 및 `@Operation`, `@Tag` 등의 어노테이션 활용 예시 제공

---

## 5. Security와 JWT 처리 요청

**프롬프트:**

> security단을 작업해야하는데, 로그인 시 access_token 반환을 해주고, 이후 요청은 Authorization: Bearer <token> 헤더 필요가 필요하니깐 필터 처리를 해줘.  
> 또한 JWT 없이 접근시 401을 응답해야하고, 유효하지 않은 ID로 접근시 404을 응답해야해.

**응답 요약:**

- JWT 토큰 생성 및 검증 로직
- Spring Security 필터 체인 구성
- 인증 실패/권한 오류에 대한 커스텀 예외 처리 (`401`, `404`)

---

## 6. 테스트 코드 작성 요청

**프롬프트:**

> 테스트 코드 작성이 필요해. 환경은 동일하게 MyBatis와 SQLite3야.  
> 기본적으로 각 API별 단위 테스트 코드 작성을 해주고, 추가로 아래에 2가지 항목에 대한 통합 테스트 코드를 작성해줘.
>
> 1. 회원가입->로그인->JWT 인증 흐름 테스트
> 2. TODO 생성->목록조회->수정->삭제 테스트

**응답 요약:**

- 각 API 단위 테스트 (MockMvc 및 Mockito 활용)
- 통합 테스트: 실제 흐름 기반 시나리오 구성
- SQLite 테스트 설정 환경 제공

---

## 7. 테스트 코드 커버리지 확인 요청

**프롬프트:**

> 현재 작성된 테스트 코드의 커버리지 확인해줘

**응답 요약:**

- JaCoCo 설정 안내
- Gradle 리포트 생성 방법 (`jacocoTestReport`)
- 커버리지 리포트 경로 및 HTML 결과 설명

---

## 8. 통합 테스트 실패 원인 분석 요청

**프롬프트:**

> AuthFlowIntegrationTest가 실패하는 원인은 뭐야?

**응답 요약:**

- 예상 원인 분석: 트랜잭션 문제, 인증 정보 누락, DB 초기화 문제 등
- 디버깅 방법 제안 (로그, @Transactional, @Rollback 확인)

---

## 9. 통합 테스트 트랜잭션 결합 요청

**프롬프트:**

> 통합테스트는 단일 테스트 메소드로 결합해서 같은 트랜잭션에서 수행되도록 수정해줘

**응답 요약:**

- 모든 흐름을 하나의 테스트 메서드에서 실행
- `@Transactional` 어노테이션으로 트랜잭션 묶음 적용

---

## 10. 테스트 코드 커버리지 재확인 요청

**프롬프트:**

> 테스트 코드 커버리지 커버리지 확인해줘

**응답 요약:**

- JaCoCo HTML 리포트 재확인
- 각 클래스/패키지별 커버리지 수치 분석 방법 제안

---

## 11. 커버리지 보완 요청

**프롬프트:**

> 커버리지가 가장 낮은 서비스 구현 패키지를 중점으로 테스트 코드 커버리지 80% 이상으로 맞춰줘

**응답 요약:**

- 미테스트 메서드 식별
- 예외 케이스 및 경계 조건에 대한 테스트 추가
- 테스트 대상 클래스별 보완 예시 제공

---

## 12. SQLite 초기화 코드 작성

**프롬프트:**

> SQLLite 초기화 코드를 작성해줘
> "/admin/database/reset" end-point로 데이터를 수동으로 초기화할 수 있어야해
> 초기화 파일은 schema.sql야

**응답 요약:**

- `DBInitializer` 클래스 구현: CommandLineRunner를 사용하여 애플리케이션 시작 시 schema.sql 실행
- schema.sql 파일 보완: 인덱스 추가 및 테이블 구조 최적화
- 데이터베이스 상태 확인 유틸리티 클래스(`DatabaseHealthChecker`) 구현
- 데이터베이스 상태 확인 API(`/admin/database/health`) 구현
- Spring Security 설정에 상태 확인 엔드포인트 접근 허용 추가
- 테스트 환경용 schema.sql 설정 분리
- `AdminService` 클래스 구현: 데이터베이스 초기화 기능을 제공하는 서비스
- 데이터베이스 초기화 API(`/admin/database/reset`) 구현: 관리자 권한으로 데이터베이스를 초기화할 수 있는 엔드포인트
- schema.sql 파일을 직접 읽어서 실행하도록 개선: 코드 중복 제거 및 유지보수성 향상

---

## 13. 코드 정리 요청

**프롬프트:**

> 구현한 전체 디렉터리 기준으로, 사용되지 않는 의존성이나 import, method 등을 정리해줘

**응답 요약:**

- build.gradle 파일에서 중복된 MyBatis 의존성 정리
- 불필요한 Jakarta EE API 의존성 제거
- 주석 처리된 자바 버전 설정 정리 (명시적 sourceCompatibility 설정)
- Controller 클래스에서 사용되지 않는 Swagger 관련 import 제거 (Content, Schema 등)
- AdminService 클래스에서 사용되지 않는 import 제거 (I/O 관련)
- application.properties의 로깅 레벨을 적절히 조정 (DEBUG → INFO)
- README.md 파일의 Spring Boot 버전 정보 수정 (3.4.5 → 3.2.3)

---

## 14. Google OAuth2 소셜 로그인 구현 요청

**프롬프트:**

> Google OAuth2 소셜 로그인을 추가하려고 해.
> 사용자가 Google로 로그인하면 서버는 Google에서 사용자 정보를 받아 DB에 저장하고, JWT 토큰을 발급해서 응답해야하고, 테스트 코드 작성이 필요해.
> 프론트엔드가 따로 구현되어있지 않아, 테스트 페이지도 하나 작성해줘.

**응답 요약:**

- Spring Boot OAuth2 Client 의존성 추가
- Google OAuth2 연동을 위한 설정 (application.properties)
- User 모델에 Google ID, 프로필 이미지, 인증 제공자 필드 추가
- 데이터베이스 스키마 업데이트
- UserMapper XML 및 인터페이스 수정
- OAuth2 인증 처리 서비스 구현
- OAuth2 컨트롤러 구현
- 테스트 HTML 페이지 생성
- OAuth2 관련 endpoint 접근 보안 설정
- 단위 테스트 코드 작성
- 테스트 환경 설정

---

## 15. OAuth2 테스트 페이지 인증 생략 요청

**프롬프트:**

> /oauth2-test 이 테스트 페이지 JWT 인증 검사 생략해줘

**응답 요약:**

- SecurityConfig 클래스 수정
- `/oauth2/**`와 함께 `/oauth2-test` 경로를 인증 생략 목록에 추가
- 보안 설정 업데이트: `.requestMatchers("/oauth2/**", "/oauth2-test").permitAll()`
- 이제 OAuth2 테스트 페이지가 인증 없이 접근 가능

---

## 16. SQLite 데이터베이스 초기화 스크립트 작성

**프롬프트:**

> 데이터 초기화를 위해 init_db.sql을 작성해주고, 해당 파일을 활용한 sqlite3 스크립트를 README.md에 넣어줘

**응답 요약:**

- init_db.sql 파일 생성: 데이터베이스 초기화를 위한 통합 SQL 스크립트
- 외래키 제약조건 활성화
- 테이블 삭제 및 재생성
- 인덱스 생성
- 샘플 데이터 삽입
- README.md에 SQLite 데이터베이스 초기화 명령어 추가: `sqlite3 database.sqlite < init_db.sql`
- 이 명령어를 통해 간단하게 데이터베이스 초기화 가능

## 17. JWT Token 응답 방식 변경

**프롬프트:**

> 로그인 성공시, 토큰 정보를 body로 내려주고있는데, 이걸 Header에서 response 하도록 변경해줘.

**응답 요약:**

- `UserController`의 login 메서드 수정: 토큰을 `Authorization` 헤더로 전달
- `ResponseEntity` 반환 타입을 `ApiResponse<Map<String, String>>`에서 `ApiResponse<Void>`로 변경
- 토큰을 `Authorization` 헤더에 "Bearer " 접두사와 함께 추가
- response body에서 토큰 정보 제거하고 성공 메시지만 포함
- 표준 인증 헤더 사용으로 보안성 향상 및 클라이언트 구현 단순화
- 로깅 시 토큰 노출 위험 감소

## 18. JWT 발급 및 검증 흐름과 OAuth2 흐름 다이어그램

**프롬프트:**

> 현재 개발된 JWT 발급 및 검증 흐름과 OAuth2 흐름을 정리 해주고, 다이어그램 형식으로 작성해줘

**응답 요약:**

- JWT 발급 및 검증 흐름 다이어그램 작성
  - 로그인 요청부터 토큰 발급까지의 과정 시각화
  - 토큰 검증 과정 및 인증 필터링 흐름 시각화
  - JwtTokenProvider, SecurityContextHolder 등 핵심 구성요소 간 상호작용 표현
- OAuth2(Google) 소셜 로그인 흐름 다이어그램 작성
  - 사용자 로그인 요청부터 Google 인증, 서버 처리까지 전체 흐름 표현
  - 신규/기존 사용자 분기 처리 로직 표현
  - JWT 토큰 발급 및 응답 과정 표현
- 다이어그램은 PlantUML 코드로 작성하여 가독성 높은 시각화 자료 제공
- 시퀀스 다이어그램 형식으로 각 컴포넌트 간 시간 흐름에 따른 상호작용 명확히 표현
- 다이어그램에 조건부 분기(alt), 활성화(activate) 등 상세 로직 포함
