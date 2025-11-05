package com.example.demo.common.security;

import com.example.demo.common.jwt.JwtAccessDeniedHandler;
import com.example.demo.common.jwt.JwtAuthenticationFilter;
import com.example.demo.common.jwt.JwtAuthenticationEntryPoint;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
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
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;

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

                // 예외 처리 핸들러 등록
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                        .accessDeniedHandler(jwtAccessDeniedHandler))

                // 요청 경로별 인가 설정
                .authorizeHttpRequests(authorize -> {
                    // 공개 URL 설정
                    Endpoint.PUBLIC_URLS.forEach((url, methods) -> {
                        for (String method : methods) {
                            authorize.requestMatchers(HttpMethod.valueOf(method), url).permitAll();
                        }
                    });
                    // ADMIN 역할 URL 설정
                    Endpoint.ADMIN_URLS.forEach((url, methods) -> {
                        for (String method : methods) {
                            authorize.requestMatchers(HttpMethod.valueOf(method), url).hasRole("ADMIN");
                        }
                    });
                    // USER 역할 URL 설정
                    Endpoint.USER_URLS.forEach((url, methods) -> {
                        for (String method : methods) {
                            authorize.requestMatchers(HttpMethod.valueOf(method), url).hasRole("USER");
                        }
                    });
                    authorize.anyRequest().denyAll(); // 명시적으로 허용된 요청 외에는 모두 차단
                })

                // JWT 필터 추가
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilter(new CorsConfig().corsFilter());

        return http.build();
    }
}