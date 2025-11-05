package com.example.demo.common.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String BEARER_PREFIX = "Bearer ";

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String jwt = resolveToken(request);

        if (StringUtils.hasText(jwt)) {
            Authentication authentication = jwtTokenProvider.getAuthentication(jwt); // 토큰을 파싱하여 인증 객체를 가져옴
            if (authentication != null) { // 인증 객체가 성공적으로 생성된 경우 (토큰이 유효한 경우)
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }

        // 유효하지 않은 토큰의 경우, 인증 정보를 설정하지 않고 다음 필터로 넘어갑니다.
        // 이렇게 하면 SecurityConfig에 정의된 permitAll() 규칙이 정상적으로 적용됩니다.
        filterChain.doFilter(request, response);
    }

    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) {
            return bearerToken.substring(7);
        }
        return null;
    }
}