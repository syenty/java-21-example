package com.example.demo.admin.config;

import com.example.demo.admin.domain.Admin;
import com.example.demo.admin.repository.AdminRepository;
import com.example.demo.common.util.ObjectUtil;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Profile({ "local", "dev" })
public class InitialAdminSetup implements CommandLineRunner {

  private final AdminRepository adminRepository;
  private final PasswordEncoder passwordEncoder;

  @Override
  public void run(String... args) throws Exception {
    // 데이터베이스에 'root@example.com' 이메일을 가진 관리자가 없는 경우에만 생성
    if (ObjectUtil.isEmpty(adminRepository.findByEmail("root@test.com"))) {
      Admin rootAdmin = Admin.builder()
          .name("RootAdmin")
          .email("root@test.com")
          .password(passwordEncoder.encode("1234")) // 실제 운영에서는 더 강력한 비밀번호 사용
          .role("ADMIN")
          .build();
      adminRepository.save(rootAdmin);
    }
  }
}