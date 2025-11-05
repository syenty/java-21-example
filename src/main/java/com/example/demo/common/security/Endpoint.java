package com.example.demo.common.security;

import java.util.Map;

/**
 * 애플리케이션의 엔드포인트 URL을 상수로 관리하는 클래스입니다.
 * 역할(Role)에 따른 접근 제어를 위해 사용됩니다.
 */
public final class Endpoint {

  // 생성자를 private으로 선언하여 인스턴스화 방지
  private Endpoint() {
  }

  /**
   * key: URL Pattern
   * value: 허용할 HTTP Methods.
   */
  public static final Map<String, String[]> PUBLIC_URLS = Map.ofEntries(
      Map.entry("/actuator/health", new String[] { "GET" }),
      Map.entry("/v3/api-docs", new String[] { "GET" }),
      Map.entry("/v3/api-docs/*", new String[] { "GET" }),
      Map.entry("/swagger-ui/*", new String[] { "GET" }),
      Map.entry("/swagger-ui.html", new String[] { "GET" }),
      Map.entry("/webjars/swagger-ui/*", new String[] { "GET" }),
      Map.entry("/api/auth/login", new String[] { "POST" }));

  public static final Map<String, String[]> ADMIN_URLS = Map.ofEntries(
      Map.entry("/api/admins", new String[] { "GET", "POST" }), // 전체 조회, 생성
      Map.entry("/api/admins/{id}", new String[] { "GET", "PUT", "DELETE" }) // 단건 조회, 수정, 삭제
  );

  public static final Map<String, String[]> USER_URLS = Map.ofEntries(
      Map.entry("/api/users", new String[] { "GET", "POST" }),
      Map.entry("/api/users/{id}", new String[] { "GET", "PUT", "DELETE" }));

  public static String[] urlPatternsFor(Map<String, String[]> endpointMap) {
    return endpointMap.keySet().toArray(new String[0]);
  }
}