-- 기존 테이블이 있다면 삭제 (테스트 환경에서는 항상 초기화)
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

-- SQLite PRAGMA 설정 (테스트 환경에서 활성화)
PRAGMA foreign_keys = ON; 