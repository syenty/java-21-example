package com.example.demo.admin.service;

import com.example.demo.admin.repository.AdminRepository;
import java.util.ArrayList;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminPrincipalService implements UserDetailsService {

    private final AdminRepository adminRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return adminRepository.findByEmail(username)
                .map(admin -> {
                    List<GrantedAuthority> authorities = new ArrayList<>();
                    // 사용자의 기본 역할을 추가합니다.
                    authorities.add(new SimpleGrantedAuthority("ROLE_" + admin.getRole()));
                    // 만약 역할이 "ADMIN"이라면, "ROLE_USER" 권한을 추가로 부여합니다.
                    if ("ADMIN".equalsIgnoreCase(admin.getRole())) {
                        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
                    }
                    return User.builder()
                            .username(admin.getEmail())
                            .password(admin.getPassword())
                            .authorities(authorities)
                            .build();
                })
                .orElseThrow(() -> new UsernameNotFoundException("Admin not found with email: " + username));
    }
}