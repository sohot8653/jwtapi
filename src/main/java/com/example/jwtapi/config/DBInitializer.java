package com.example.jwtapi.config;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;

@Configuration
public class DBInitializer {
    
    private static final Logger logger = LoggerFactory.getLogger(DBInitializer.class);
    
    /**
     * 애플리케이션 시작 시 데이터베이스 초기화를 확인하는 CommandLineRunner 빈
     * schema.sql 파일을 명시적으로 실행하여 테이블 구조를 생성합니다.
     */
    @Bean
    public CommandLineRunner initDatabase(DataSource dataSource) {
        return args -> {
            logger.info("데이터베이스 초기화 실행 중...");
            
            try {
                // schema.sql 파일을 사용하여 데이터베이스 스키마 생성
                ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
                populator.addScript(new ClassPathResource("schema.sql"));
                populator.execute(dataSource);
                
                logger.info("데이터베이스 초기화 완료!");
            } catch (Exception e) {
                logger.error("데이터베이스 초기화 중 오류 발생: {}", e.getMessage(), e);
                throw e;
            }
        };
    }
} 
 