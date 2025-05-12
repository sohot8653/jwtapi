package com.example.jwtapi.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import com.example.jwtapi.dto.ApiResponse;
import com.example.jwtapi.dto.TodoDTO;
import com.example.jwtapi.service.TodoService;
import com.example.jwtapi.vo.todo.TodoCreateVO;
import com.example.jwtapi.vo.todo.TodoSearchVO;
import com.example.jwtapi.vo.todo.TodoUpdateVO;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/todos")
@RequiredArgsConstructor
@Tag(name = "Todo", description = "Todo 관련 API")
public class TodoController {

    private final TodoService todoService;

    @Operation(summary = "Todo 생성", description = "새로운 Todo를 생성합니다.")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "생성 성공"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "잘못된 요청"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "인증되지 않은 사용자")
    })
    @SecurityRequirement(name = "bearerAuth")
    @PostMapping
    public ResponseEntity<ApiResponse<TodoDTO>> createTodo(
            @Parameter(hidden = true) @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody TodoCreateVO todoCreateVO) {
        TodoDTO todoDTO = todoService.createTodo(userDetails.getUsername(), todoCreateVO);
        return ResponseEntity.ok(ApiResponse.success("Todo가 생성되었습니다.", todoDTO));
    }

    @Operation(summary = "Todo 목록 조회", description = "사용자의 모든 Todo를 조회합니다.")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "조회 성공"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "인증되지 않은 사용자")
    })
    @SecurityRequirement(name = "bearerAuth")
    @GetMapping
    public ResponseEntity<ApiResponse<List<TodoDTO>>> getAllTodos(
            @Parameter(hidden = true) @AuthenticationPrincipal UserDetails userDetails) {
        List<TodoDTO> todos = todoService.getAllTodos(userDetails.getUsername());
        return ResponseEntity.ok(ApiResponse.success(todos));
    }

    @Operation(summary = "Todo 상세 조회", description = "특정 Todo의 상세 정보를 조회합니다.")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "조회 성공"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "인증되지 않은 사용자"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Todo를 찾을 수 없음")
    })
    @SecurityRequirement(name = "bearerAuth")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<TodoDTO>> getTodoById(
            @Parameter(hidden = true) @AuthenticationPrincipal UserDetails userDetails,
            @Parameter(description = "Todo ID", required = true) @PathVariable Long id) {
        TodoDTO todoDTO = todoService.getTodoById(userDetails.getUsername(), id);
        return ResponseEntity.ok(ApiResponse.success(todoDTO));
    }

    @Operation(summary = "Todo 수정", description = "특정 Todo를 수정합니다.")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "수정 성공"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "잘못된 요청"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "인증되지 않은 사용자"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Todo를 찾을 수 없음")
    })
    @SecurityRequirement(name = "bearerAuth")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<TodoDTO>> updateTodo(
            @Parameter(hidden = true) @AuthenticationPrincipal UserDetails userDetails,
            @Parameter(description = "Todo ID", required = true) @PathVariable Long id,
            @Valid @RequestBody TodoUpdateVO todoUpdateVO) {
        TodoDTO todoDTO = todoService.updateTodo(userDetails.getUsername(), id, todoUpdateVO);
        return ResponseEntity.ok(ApiResponse.success("Todo가 업데이트되었습니다.", todoDTO));
    }

    @Operation(summary = "Todo 삭제", description = "특정 Todo를 삭제합니다.")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "삭제 성공"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "인증되지 않은 사용자"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Todo를 찾을 수 없음")
    })
    @SecurityRequirement(name = "bearerAuth")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteTodo(
            @Parameter(hidden = true) @AuthenticationPrincipal UserDetails userDetails,
            @Parameter(description = "Todo ID", required = true) @PathVariable Long id) {
        todoService.deleteTodo(userDetails.getUsername(), id);
        return ResponseEntity.ok(ApiResponse.success("Todo가 삭제되었습니다.", null));
    }

    @Operation(summary = "Todo 검색", description = "키워드 및 완료 상태로 Todo를 검색합니다.")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "검색 성공"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "인증되지 않은 사용자")
    })
    @SecurityRequirement(name = "bearerAuth")
    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<TodoDTO>>> searchTodos(
            @Parameter(hidden = true) @AuthenticationPrincipal UserDetails userDetails,
            @Parameter(description = "검색 조건") @ModelAttribute TodoSearchVO todoSearchVO) {
        List<TodoDTO> todos = todoService.searchTodos(userDetails.getUsername(), todoSearchVO);
        return ResponseEntity.ok(ApiResponse.success(todos));
    }
} 