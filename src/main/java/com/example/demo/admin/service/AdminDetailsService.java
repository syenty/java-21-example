package com.example.demo.admin.service;

import com.example.demo.admin.repository.AdminRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminDetailsService implements UserDetailsService {

    private final AdminRepository adminRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return adminRepository.findByEmail(username)
                .map(admin -> User.builder()
                        .username(admin.getEmail())
                        .password(admin.getPassword())
                        .authorities(List.of(new SimpleGrantedAuthority("ROLE_" + admin.getRole())))
                        .build())
                .orElseThrow(() -> new UsernameNotFoundException("Admin not found with email: " + username));
    }
}