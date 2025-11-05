package com.example.demo.common.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

@Component
public class JwtTokenProvider {

    private static final String AUTHORITIES_KEY = "auth";
    private final SecretKey key;    
    private final long accessTokenValidityInMilliseconds;

    public JwtTokenProvider(@Value("${jwt.secret}") String secretKey,
                            @Value("${jwt.access-token-validity-in-seconds}") long accessTokenValidityInSeconds) {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
        this.accessTokenValidityInMilliseconds = accessTokenValidityInSeconds * 1000;
    }

    public String createToken(Authentication authentication) {
        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        long now = (new Date()).getTime();
        Date validity = new Date(now + this.accessTokenValidityInMilliseconds);

        return Jwts.builder()
                .subject(authentication.getName())
                .claim(AUTHORITIES_KEY, authorities)
                .signWith(key)
                .expiration(validity)
                .compact();
    }

    public Authentication getAuthentication(String token) {
        try {
            Claims claims = Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload();

            Collection<? extends GrantedAuthority> authorities =
                    Arrays.stream(claims.get(AUTHORITIES_KEY).toString().split(","))
                            .map(SimpleGrantedAuthority::new)
                            .collect(Collectors.toList());

            UserDetails principal = new User(claims.getSubject(), "", authorities);

            return new UsernamePasswordAuthenticationToken(principal, token, authorities);
        } catch (Exception e) {
            // MalformedJwtException, ExpiredJwtException, UnsupportedJwtException, IllegalArgumentException
            // 토큰 파싱 실패 시 null을 반환하여 유효하지 않은 토큰임을 알림
            return null;
        }
    }
}