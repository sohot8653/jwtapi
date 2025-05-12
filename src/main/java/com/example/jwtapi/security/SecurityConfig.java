package com.example.jwtapi.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity  // @PreAuthorize 지원을 위해 추가
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtTokenProvider jwtTokenProvider;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final ObjectMapper objectMapper;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // 인증이 필요 없는 경로 정의
        RequestMatcher[] permitAllMatchers = {
            new AntPathRequestMatcher("/users/signup"),
            new AntPathRequestMatcher("/users/login"),
            new AntPathRequestMatcher("/swagger-ui/**"),
            new AntPathRequestMatcher("/v3/api-docs/**"),
            new AntPathRequestMatcher("/swagger-ui.html"),
            new AntPathRequestMatcher("/oauth2/**"),
            new AntPathRequestMatcher("/oauth2-test"),
            new AntPathRequestMatcher("/oauth2-test.html"),
            new AntPathRequestMatcher("/static/**"),
            new AntPathRequestMatcher("/*.html"),
            new AntPathRequestMatcher("/*.js"),
            new AntPathRequestMatcher("/*.css"),
            new AntPathRequestMatcher("/*.ico")
        };
        
        http
            .csrf(csrf -> csrf.disable())
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .exceptionHandling(exceptions -> exceptions
                .authenticationEntryPoint(jwtAuthenticationEntryPoint))
            .authorizeHttpRequests(authorize -> {
                // 인증이 필요 없는 경로 설정
                for (RequestMatcher matcher : permitAllMatchers) {
                    authorize.requestMatchers(matcher).permitAll();
                }
                // 나머지 모든 요청은 인증 필요
                authorize.anyRequest().authenticated();
            })
            .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider, objectMapper, permitAllMatchers), UsernamePasswordAuthenticationFilter.class);
        
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
} 