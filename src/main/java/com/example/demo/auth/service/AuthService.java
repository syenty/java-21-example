package com.example.demo.auth.service;

import com.example.demo.common.auth.dto.LoginRequest;
import com.example.demo.common.auth.dto.TokenResponse;
import com.example.demo.common.auth.dto.UserEmployeeLoginRequest;
import com.example.demo.user.domain.User;
import com.example.demo.user.service.UserService;
import com.example.demo.common.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserService userService;

    public TokenResponse login(LoginRequest loginRequest) {
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword());

        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        String accessToken = jwtTokenProvider.createToken(authentication);
        return new TokenResponse(accessToken);
    }

    public TokenResponse userLogin(UserEmployeeLoginRequest request) {
        User user = userService.getByEmployeeNumberAndName(request.getEmployeeNumber(), request.getName())
                .orElseThrow(() -> new IllegalArgumentException("User not found for provided credentials"));

        Authentication authentication = new UsernamePasswordAuthenticationToken(
                user.getExternalId(),
                null,
                List.of(new SimpleGrantedAuthority("ROLE_USER"))
        );

        String accessToken = jwtTokenProvider.createToken(authentication);
        return new TokenResponse(accessToken);
    }
}
