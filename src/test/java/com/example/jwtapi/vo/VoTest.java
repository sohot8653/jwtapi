package com.example.jwtapi.vo;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import com.example.jwtapi.vo.todo.TodoCreateVO;
import com.example.jwtapi.vo.todo.TodoSearchVO;
import com.example.jwtapi.vo.todo.TodoUpdateVO;
import com.example.jwtapi.vo.user.UserLoginVO;
import com.example.jwtapi.vo.user.UserSignupVO;
import com.example.jwtapi.vo.user.UserUpdateVO;

import java.lang.reflect.Field;
import java.util.List;

class VoTest {

    @Test
    void testUserSignupVO() {
        UserSignupVO vo = new UserSignupVO();
        
        // Setter 테스트
        String username = "testuser";
        String password = "password";
        String email = "test@example.com";
        String name = "Test User";
        
        vo.setUsername(username);
        vo.setPassword(password);
        vo.setEmail(email);
        vo.setName(name);
        
        // Getter 테스트
        assertEquals(username, vo.getUsername());
        assertEquals(password, vo.getPassword());
        assertEquals(email, vo.getEmail());
        assertEquals(name, vo.getName());
        
        // toString 테스트
        assertNotNull(vo.toString());
        assertTrue(vo.toString().contains(username));
        assertTrue(vo.toString().contains(email));
        
        // equals 및 hashCode 테스트
        UserSignupVO vo2 = new UserSignupVO();
        vo2.setUsername(username);
        vo2.setPassword(password);
        vo2.setEmail(email);
        vo2.setName(name);
        
        assertEquals(vo, vo2);
        assertEquals(vo.hashCode(), vo2.hashCode());
        
        // 다른 값으로 설정시 equals 테스트
        UserSignupVO vo3 = new UserSignupVO();
        vo3.setUsername("differentUser");
        vo3.setPassword(password);
        vo3.setEmail(email);
        vo3.setName(name);
        
        assertNotEquals(vo, vo3);
        
        // null 값 테스트
        UserSignupVO vo4 = new UserSignupVO();
        vo4.setUsername(null);
        vo4.setPassword(null);
        vo4.setEmail(null);
        vo4.setName(null);
        
        assertNull(vo4.getUsername());
        assertNull(vo4.getPassword());
        assertNull(vo4.getEmail());
        assertNull(vo4.getName());
    }
    
    @Test
    void testUserLoginVO() {
        UserLoginVO vo = new UserLoginVO();
        
        // Setter 테스트
        String username = "testuser";
        String password = "password";
        
        vo.setUsername(username);
        vo.setPassword(password);
        
        // Getter 테스트
        assertEquals(username, vo.getUsername());
        assertEquals(password, vo.getPassword());
        
        // toString 테스트
        assertNotNull(vo.toString());
        assertTrue(vo.toString().contains(username));
        
        // equals 및 hashCode 테스트
        UserLoginVO vo2 = new UserLoginVO();
        vo2.setUsername(username);
        vo2.setPassword(password);
        
        assertEquals(vo, vo2);
        assertEquals(vo.hashCode(), vo2.hashCode());
        
        // 다른 값으로 설정시 equals 테스트
        UserLoginVO vo3 = new UserLoginVO();
        vo3.setUsername(username);
        vo3.setPassword("differentPassword");
        
        assertNotEquals(vo, vo3);
        
        // null 값 테스트
        UserLoginVO vo4 = new UserLoginVO();
        vo4.setUsername(null);
        vo4.setPassword(null);
        
        assertNull(vo4.getUsername());
        assertNull(vo4.getPassword());
    }
    
    @Test
    void testUserUpdateVO() {
        UserUpdateVO vo = new UserUpdateVO();
        
        // Setter 테스트
        String password = "newpassword";
        String email = "updated@example.com";
        String name = "Updated User";
        
        vo.setPassword(password);
        vo.setEmail(email);
        vo.setName(name);
        
        // Getter 테스트
        assertEquals(password, vo.getPassword());
        assertEquals(email, vo.getEmail());
        assertEquals(name, vo.getName());
        
        // toString 테스트
        assertNotNull(vo.toString());
        assertTrue(vo.toString().contains(email));
        assertTrue(vo.toString().contains(name));
        
        // equals 및 hashCode 테스트
        UserUpdateVO vo2 = new UserUpdateVO();
        vo2.setPassword(password);
        vo2.setEmail(email);
        vo2.setName(name);
        
        assertEquals(vo, vo2);
        assertEquals(vo.hashCode(), vo2.hashCode());
        
        // 다른 값으로 설정시 equals 테스트
        UserUpdateVO vo3 = new UserUpdateVO();
        vo3.setPassword("differentPassword");
        vo3.setEmail(email);
        vo3.setName(name);
        
        assertNotEquals(vo, vo3);
        
        // null 값 허용 테스트 - 부분 업데이트 시나리오
        UserUpdateVO vo4 = new UserUpdateVO();
        vo4.setPassword(null);
        vo4.setEmail("partial@update.com");
        vo4.setName(null);
        
        assertNull(vo4.getPassword());
        assertEquals("partial@update.com", vo4.getEmail());
        assertNull(vo4.getName());
    }
    
    @Test
    void testTodoCreateVO() {
        TodoCreateVO vo = new TodoCreateVO();
        
        // Setter 테스트
        String title = "Test Todo";
        String content = "Test Content";
        boolean completed = false;
        
        vo.setTitle(title);
        vo.setContent(content);
        vo.setCompleted(completed);
        
        // Getter 테스트
        assertEquals(title, vo.getTitle());
        assertEquals(content, vo.getContent());
        assertEquals(completed, vo.isCompleted());
        
        // toString 테스트
        assertNotNull(vo.toString());
        assertTrue(vo.toString().contains(title));
        assertTrue(vo.toString().contains(content));
        
        // equals 및 hashCode 테스트
        TodoCreateVO vo2 = new TodoCreateVO();
        vo2.setTitle(title);
        vo2.setContent(content);
        vo2.setCompleted(completed);
        
        assertEquals(vo, vo2);
        assertEquals(vo.hashCode(), vo2.hashCode());
        
        // 다른 값으로 설정시 equals 테스트
        TodoCreateVO vo3 = new TodoCreateVO();
        vo3.setTitle("Different Title");
        vo3.setContent(content);
        vo3.setCompleted(completed);
        
        assertNotEquals(vo, vo3);
        
        // content null 테스트
        TodoCreateVO vo4 = new TodoCreateVO();
        vo4.setTitle(title);
        vo4.setContent(null);
        vo4.setCompleted(completed);
        
        assertNull(vo4.getContent());
        assertEquals(title, vo4.getTitle());
        assertEquals(completed, vo4.isCompleted());
    }
    
    @Test
    void testTodoUpdateVO() {
        TodoUpdateVO vo = new TodoUpdateVO();
        
        // Setter 테스트
        String title = "Updated Todo";
        String content = "Updated Content";
        Boolean completed = true;
        
        vo.setTitle(title);
        vo.setContent(content);
        vo.setCompleted(completed);
        
        // Getter 테스트
        assertEquals(title, vo.getTitle());
        assertEquals(content, vo.getContent());
        assertEquals(completed, vo.getCompleted());
        
        // toString 테스트
        assertNotNull(vo.toString());
        assertTrue(vo.toString().contains(title));
        assertTrue(vo.toString().contains(content));
        
        // equals 및 hashCode 테스트
        TodoUpdateVO vo2 = new TodoUpdateVO();
        vo2.setTitle(title);
        vo2.setContent(content);
        vo2.setCompleted(completed);
        
        assertEquals(vo, vo2);
        assertEquals(vo.hashCode(), vo2.hashCode());
        
        // 다른 값으로 설정시 equals 테스트
        TodoUpdateVO vo3 = new TodoUpdateVO();
        vo3.setTitle("Different Title");
        vo3.setContent(content);
        vo3.setCompleted(completed);
        
        assertNotEquals(vo, vo3);
        
        // 부분 업데이트 시나리오 테스트
        TodoUpdateVO vo4 = new TodoUpdateVO();
        vo4.setTitle("Title Only");
        vo4.setContent(null);
        vo4.setCompleted(null);
        
        assertEquals("Title Only", vo4.getTitle());
        assertNull(vo4.getContent());
        assertNull(vo4.getCompleted());
    }
    
    @Test
    void testTodoSearchVO() {
        TodoSearchVO vo = new TodoSearchVO();
        
        // Setter 테스트
        String keyword = "search term";
        Boolean completed = true;
        
        vo.setKeyword(keyword);
        vo.setCompleted(completed);
        
        // Getter 테스트
        assertEquals(keyword, vo.getKeyword());
        assertEquals(completed, vo.getCompleted());
        
        // toString 테스트
        assertNotNull(vo.toString());
        assertTrue(vo.toString().contains(keyword));
        
        // 추가 테스트 (널 값 설정)
        vo.setKeyword(null);
        vo.setCompleted(null);
        assertNull(vo.getKeyword());
        assertNull(vo.getCompleted());
        
        // equals 및 hashCode 테스트
        TodoSearchVO vo2 = new TodoSearchVO();
        vo2.setKeyword("test keyword");
        vo2.setCompleted(false);
        
        TodoSearchVO vo3 = new TodoSearchVO();
        vo3.setKeyword("test keyword");
        vo3.setCompleted(false);
        
        assertEquals(vo2, vo3);
        assertEquals(vo2.hashCode(), vo3.hashCode());
        
        // 다른 값으로 설정시 equals 테스트
        TodoSearchVO vo4 = new TodoSearchVO();
        vo4.setKeyword("different keyword");
        vo4.setCompleted(false);
        
        assertNotEquals(vo2, vo4);
        
        // 복합 검색 테스트 (키워드만 있는 경우)
        TodoSearchVO keywordOnly = new TodoSearchVO();
        keywordOnly.setKeyword("keyword only");
        keywordOnly.setCompleted(null);
        
        assertEquals("keyword only", keywordOnly.getKeyword());
        assertNull(keywordOnly.getCompleted());
        
        // 복합 검색 테스트 (상태만 있는 경우)
        TodoSearchVO completedOnly = new TodoSearchVO();
        completedOnly.setKeyword(null);
        completedOnly.setCompleted(true);
        
        assertNull(completedOnly.getKeyword());
        assertTrue(completedOnly.getCompleted());
    }
    
    @Test
    void testVoHaveGettersAndSetters() {
        // 각 VO 클래스가 getter/setter 메서드를 가지고 있는지 확인
        
        // UserSignupVO
        UserSignupVO userSignupVO = new UserSignupVO();
        assertNotNull(userSignupVO);
        
        // UserLoginVO
        UserLoginVO userLoginVO = new UserLoginVO();
        assertNotNull(userLoginVO);
        
        // UserUpdateVO
        UserUpdateVO userUpdateVO = new UserUpdateVO();
        assertNotNull(userUpdateVO);
        
        // TodoCreateVO
        TodoCreateVO todoCreateVO = new TodoCreateVO();
        assertNotNull(todoCreateVO);
        
        // TodoUpdateVO
        TodoUpdateVO todoUpdateVO = new TodoUpdateVO();
        assertNotNull(todoUpdateVO);
        
        // TodoSearchVO
        TodoSearchVO todoSearchVO = new TodoSearchVO();
        assertNotNull(todoSearchVO);
    }
    
    @Test
    void testVoEqualityWithReflection() throws Exception {
        // 리플렉션을 사용하여 모든 필드 값이 동일할 때 equals가 true를 반환하는지 테스트
        
        // UserSignupVO 테스트
        UserSignupVO signup1 = new UserSignupVO();
        signup1.setUsername("testuser");
        signup1.setPassword("password");
        signup1.setEmail("test@example.com");
        signup1.setName("Test User");
        
        UserSignupVO signup2 = new UserSignupVO();
        signup2.setUsername("testuser");
        signup2.setPassword("password");
        signup2.setEmail("test@example.com");
        signup2.setName("Test User");
        
        assertTrue(signup1.equals(signup2));
        
        // 필드 하나씩 변경하며 테스트
        for (Field field : UserSignupVO.class.getDeclaredFields()) {
            field.setAccessible(true);
            
            // 원래 값 저장
            Object originalValue = field.get(signup2);
            
            // 값 변경
            if (field.getType() == String.class) {
                field.set(signup2, "changed_" + field.getName());
            }
            
            // equals가 false를 반환하는지 확인
            assertFalse(signup1.equals(signup2), "Changing " + field.getName() + " should make objects not equal");
            
            // 원래 값으로 복원
            field.set(signup2, originalValue);
            
            // 다시 equals가 true를 반환하는지 확인
            assertTrue(signup1.equals(signup2), "Restoring " + field.getName() + " should make objects equal again");
        }
    }
    
    @Test
    void testEdgeCases() {
        // 경계값 테스트
        
        // 빈 문자열 테스트
        TodoCreateVO emptyTodo = new TodoCreateVO();
        emptyTodo.setTitle("");
        emptyTodo.setContent("");
        emptyTodo.setCompleted(false);
        
        assertEquals("", emptyTodo.getTitle());
        assertEquals("", emptyTodo.getContent());
        
        // 긴 문자열 테스트
        StringBuilder longString = new StringBuilder();
        for (int i = 0; i < 1000; i++) {
            longString.append("a");
        }
        
        UserUpdateVO longValueUser = new UserUpdateVO();
        longValueUser.setName(longString.toString());
        longValueUser.setEmail(longString.substring(0, 900) + "@example.com");
        longValueUser.setPassword(longString.substring(0, 100));
        
        assertEquals(longString.toString(), longValueUser.getName());
        assertEquals(longString.substring(0, 900) + "@example.com", longValueUser.getEmail());
        assertEquals(longString.substring(0, 100), longValueUser.getPassword());
    }
} 