package com.example.demo.auth.service;

import com.example.demo.common.auth.dto.LoginRequest;
import com.example.demo.common.auth.dto.TokenResponse;
import com.example.demo.common.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final JwtTokenProvider jwtTokenProvider;

    public TokenResponse login(LoginRequest loginRequest) {
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword());

        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        String accessToken = jwtTokenProvider.createToken(authentication);
        return new TokenResponse(accessToken);
    }
}