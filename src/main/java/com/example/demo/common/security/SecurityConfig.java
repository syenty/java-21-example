package com.example.demo.common.security;

import com.example.demo.common.jwt.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // CSRF 보호 비활성화 (Stateless API의 경우)
                .csrf(AbstractHttpConfigurer::disable)

                // HTTP Basic 인증 비활성화
                .httpBasic(AbstractHttpConfigurer::disable)

                // Form Login 비활성화
                .formLogin(AbstractHttpConfigurer::disable)

                // 세션 관리 정책을 STATELESS로 설정 (JWT 기반 인증에 적합)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // 요청 경로별 인가 설정
                .authorizeHttpRequests(auth -> auth
                        // Swagger UI 및 API 문서 경로는 모두 허용
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/api/auth/login").permitAll()
                        // /api/admins/** 경로는 ADMIN 역할을 가진 사용자만 접근 가능
                        .requestMatchers("/api/admins/**").hasRole("ADMIN")
                        // /api/users/** 경로는 USER 역할을 가진 사용자만 접근 가능 (예시)
                        // .requestMatchers("/api/users/**").hasRole("USER")
                        // 그 외 나머지 모든 요청은 인증만 되면 접근 가능
                        .anyRequest().authenticated())

                // JWT 필터 추가
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}