package com.example.jwtapi.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.jwtapi.dto.ApiResponse;
import com.example.jwtapi.util.DatabaseHealthChecker;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

/**
 * 관리자 기능을 위한 컨트롤러
 */
@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
@Tag(name = "Admin", description = "관리자 기능 API")
public class AdminController {

    private final DatabaseHealthChecker databaseHealthChecker;
    
    /**
     * 데이터베이스 상태를 확인합니다.
     * 현재는 보안을 적용하지 않았지만, 실제 운영 환경에서는 관리자 권한을 요구해야 합니다.
     */
    @Operation(summary = "데이터베이스 상태 확인", description = "SQLite 데이터베이스의 상태를 확인합니다.")
    @GetMapping("/database/health")
    // 주석 처리된 부분은 실제 관리자 권한이 구현된 후 사용
    // @PreAuthorize("hasRole('ADMIN')")
    // @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<ApiResponse<Map<String, Object>>> checkDatabaseHealth() {
        Map<String, Object> healthStatus = databaseHealthChecker.checkDatabaseHealth();
        boolean isUp = "UP".equals(healthStatus.get("status"));
        
        if (isUp) {
            return ResponseEntity.ok(ApiResponse.success("데이터베이스가 정상 동작 중입니다.", healthStatus));
        } else {
            return ResponseEntity.status(503) // Service Unavailable
                    .body(ApiResponse.error("데이터베이스에 문제가 있습니다: " + healthStatus.get("error"), healthStatus));
        }
    }
} 
 