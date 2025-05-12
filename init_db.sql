-- SQLite 데이터베이스 통합 초기화 스크립트
-- 이 파일은 schema.sql과 data.sql의 내용을 결합하여 한 번에 실행할 수 있게 합니다.

-- 외래키 제약 조건 활성화
PRAGMA foreign_keys = ON;

-- 기존 테이블 삭제 (필요시 주석 해제)
DROP TABLE IF EXISTS todos;
DROP TABLE IF EXISTS users;

-- 사용자 테이블
CREATE TABLE IF NOT EXISTS users (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(100),
    email VARCHAR(100) NOT NULL,
    name VARCHAR(100) NOT NULL,
    google_id VARCHAR(100),
    profile_image VARCHAR(255),
    auth_provider VARCHAR(20),
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);

-- 사용자 이메일 인덱스
CREATE INDEX IF NOT EXISTS idx_users_email ON users(email);
-- 사용자 생성일 인덱스
CREATE INDEX IF NOT EXISTS idx_users_created_at ON users(created_at);

-- Todo 테이블
CREATE TABLE IF NOT EXISTS todos (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    user_id INTEGER NOT NULL,
    title VARCHAR(100) NOT NULL,
    content TEXT,
    completed BOOLEAN NOT NULL DEFAULT 0,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- Todo 사용자 ID 인덱스
CREATE INDEX IF NOT EXISTS idx_todos_user_id ON todos(user_id);
-- Todo 완료 상태 인덱스
CREATE INDEX IF NOT EXISTS idx_todos_completed ON todos(completed);
-- Todo 생성일 인덱스
CREATE INDEX IF NOT EXISTS idx_todos_created_at ON todos(created_at);

-- 여기서부터 데이터 초기화 (data.sql) 내용
-- 필요 없는 경우 아래 INSERT 문들을 주석 처리하거나 삭제하세요

-- 테스트용 사용자 추가 (비밀번호: password123)
INSERT OR IGNORE INTO users (username, password, email, name, auth_provider, created_at, updated_at)
VALUES ('testuser', '$2a$10$eDIJJHvLmdNZGKAHNVW4ruMvdto8O7oAUjDJpjQzKjbLCdxHsLc3W', 'test@example.com', '테스트 사용자', 'LOCAL', datetime('now'), datetime('now'));

-- 테스트용 Todo 항목 추가
INSERT INTO todos (user_id, title, content, completed, created_at, updated_at)
VALUES (1, '첫 번째 할 일', '이것은 첫 번째 할 일 항목입니다.', 0, datetime('now'), datetime('now'));

INSERT INTO todos (user_id, title, content, completed, created_at, updated_at)
VALUES (1, '두 번째 할 일', '이것은 두 번째 할 일 항목입니다.', 0, datetime('now'), datetime('now'));

-- Google OAuth 로그인 테스트 사용자 (선택 사항)
INSERT OR IGNORE INTO users (username, email, name, google_id, profile_image, auth_provider, created_at, updated_at)
VALUES ('googleuser', 'google@example.com', 'Google 사용자', '123456789', 'https://example.com/profile.jpg', 'GOOGLE', datetime('now'), datetime('now'));

-- 데이터베이스 초기화 후 확인 명령어 (참고용, 이 파일에 포함되지 않음)
-- .tables
-- SELECT * FROM users;
-- SELECT * FROM todos; 