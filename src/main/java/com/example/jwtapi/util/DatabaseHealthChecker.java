package com.example.jwtapi.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * SQLite 데이터베이스 연결 상태를 모니터링하는 유틸리티 클래스
 */
@Component
public class DatabaseHealthChecker {

    private static final Logger logger = LoggerFactory.getLogger(DatabaseHealthChecker.class);
    private final DataSource dataSource;

    public DatabaseHealthChecker(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    /**
     * 데이터베이스 상태를 확인합니다.
     * @return 상태 정보를 담은 맵
     */
    public Map<String, Object> checkDatabaseHealth() {
        Map<String, Object> status = new HashMap<>();
        
        try (Connection conn = dataSource.getConnection()) {
            // 데이터베이스 상태 확인
            return checkUserTable(conn);
        } catch (SQLException e) {
            logger.error("데이터베이스 연결 실패: {}", e.getMessage());
            status.put("status", "DOWN");
            status.put("error", e.getMessage());
            return status;
        }
    }

    private Map<String, Object> checkUserTable(Connection conn) {
        Map<String, Object> status = new HashMap<>();
        String sql = "SELECT COUNT(*) FROM sqlite_master WHERE type='table' AND name='users'";
        
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            if (rs.next() && rs.getInt(1) > 0) {
                // users 테이블이 존재함
                return checkDatabaseStructure(conn);
            } else {
                // users 테이블이 존재하지 않음
                logger.warn("사용자 테이블이 초기화되지 않았습니다");
                status.put("status", "DOWN");
                status.put("error", "사용자 테이블이 존재하지 않습니다");
                return status;
            }
        } catch (SQLException e) {
            logger.error("테이블 확인 중 오류 발생: {}", e.getMessage());
            status.put("status", "DOWN");
            status.put("error", e.getMessage());
            return status;
        }
    }
    
    private Map<String, Object> checkDatabaseStructure(Connection conn) {
        Map<String, Object> status = new HashMap<>();
        
        try {
            // 인덱스 정보 확인
            boolean usersIndexExists = checkIndexExists(conn, "idx_users_email");
            boolean todosIndexExists = checkIndexExists(conn, "idx_todos_user_id");
            
            // 모든 검사 통과
            status.put("status", "UP");
            status.put("database", "SQLite");
            status.put("usersTable", "OK");
            status.put("usersIndexes", usersIndexExists ? "OK" : "Missing");
            status.put("todosIndexes", todosIndexExists ? "OK" : "Missing");
            return status;
        } catch (SQLException e) {
            logger.error("데이터베이스 구조 확인 중 오류 발생: {}", e.getMessage());
            status.put("status", "DOWN");
            status.put("error", e.getMessage());
            return status;
        }
    }
    
    private boolean checkIndexExists(Connection conn, String indexName) throws SQLException {
        String sql = "SELECT COUNT(*) FROM sqlite_master WHERE type='index' AND name=?";
        
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, indexName);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        }
    }
} 
 