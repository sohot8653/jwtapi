package com.example.jwtapi.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.FileCopyUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class AdminService {

    @Value("${spring.datasource.url}")
    private String dbUrl;

    @Transactional
    public void resetDatabase() {
        try {
            // 데이터베이스 연결
            try (Connection conn = DriverManager.getConnection(dbUrl);
                 Statement stmt = conn.createStatement()) {
                
                // 외래 키 제약 조건 비활성화
                stmt.execute("PRAGMA foreign_keys = OFF;");
                
                // 테이블 삭제
                stmt.execute("DROP TABLE IF EXISTS todos;");
                stmt.execute("DROP TABLE IF EXISTS users;");
                
                // 외래 키 제약 조건 활성화
                stmt.execute("PRAGMA foreign_keys = ON;");
                
                // schema.sql 파일 읽기
                String schemaSql = new String(FileCopyUtils.copyToByteArray(
                    new ClassPathResource("schema.sql").getInputStream()));
                
                // SQL 문장 분리 및 실행
                List<String> sqlStatements = Arrays.stream(schemaSql.split(";"))
                    .map(String::trim)
                    .filter(sql -> !sql.isEmpty() && !sql.startsWith("--"))
                    .collect(Collectors.toList());
                
                for (String sql : sqlStatements) {
                    stmt.execute(sql);
                }
            }
            
            log.info("Database has been reset successfully using schema.sql");
        } catch (Exception e) {
            log.error("Error resetting database", e);
            throw new RuntimeException("데이터베이스 초기화 중 오류가 발생했습니다.", e);
        }
    }
} 