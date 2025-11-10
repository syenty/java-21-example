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
      Map.entry("/api/auth/admin-login", new String[] { "POST" }),
      Map.entry("/api/auth/user-login", new String[] { "POST" }),
      Map.entry("/api/auth/event-login", new String[] { "POST" }));

  public static final Map<String, String[]> ADMIN_URLS = Map.ofEntries(
      Map.entry("/api/admins", new String[] { "GET", "POST" }),
      Map.entry("/api/admins/{id}", new String[] { "GET", "PUT", "DELETE" }),
      Map.entry("/api/users", new String[] { "GET", "POST" }),
      Map.entry("/api/users/{id}", new String[] { "GET", "PUT", "DELETE" }),
      Map.entry("/api/events", new String[] { "GET", "POST" }),
      Map.entry("/api/events/{id}", new String[] { "GET", "PUT", "DELETE" }),
      Map.entry("/api/events/{eventId}/sequences", new String[] { "GET", "POST" }),
      Map.entry("/api/events/{eventId}/sequences/{seqDate}", new String[] { "GET", "PUT", "DELETE" }),
      Map.entry("/api/quizzes", new String[] { "GET", "POST" }),
      Map.entry("/api/quizzes/{id}", new String[] { "GET", "PUT", "DELETE" }),

      Map.entry("/api/quiz-options", new String[] { "GET", "POST" }),
      Map.entry("/api/quiz-options/{id}", new String[] { "GET", "PUT", "DELETE" }),
      Map.entry("/api/quiz-participations", new String[] { "GET", "POST" }),
      Map.entry("/api/quiz-participations/{id}", new String[] { "GET", "PUT", "DELETE" }),
      Map.entry("/api/quiz-participation-answers", new String[] { "GET", "POST" }),
      Map.entry("/api/quiz-participation-answers/{id}", new String[] { "GET", "PUT", "DELETE" }),
      Map.entry("/api/reward-policies", new String[] { "GET", "POST" }),
      Map.entry("/api/reward-policies/{id}", new String[] { "GET", "PUT", "DELETE" }),
      Map.entry("/api/reward-issues", new String[] { "GET", "POST" }),
      Map.entry("/api/reward-issues/{id}", new String[] { "GET", "PUT", "DELETE" }));

  public static final Map<String, String[]> USER_URLS = Map.ofEntries(
      Map.entry("/api/users/external/{externalId}", new String[] { "GET" }),
      Map.entry("/api/events", new String[] { "GET" }),
      Map.entry("/api/events/{id}", new String[] { "GET" }),
      Map.entry("/api/events/{eventId}/sequences", new String[] { "GET" }),
      Map.entry("/api/events/{eventId}/sequences/{seqDate}", new String[] { "GET" }),
      Map.entry("/api/quizzes", new String[] { "GET" }),
      Map.entry("/api/quizzes/{id}", new String[] { "GET" }),
      Map.entry("/api/quizzes/events/{eventId}/today", new String[] { "GET" }),
      Map.entry("/api/quiz-participations", new String[] { "GET" }),
      Map.entry("/api/quiz-participations/{id}", new String[] { "GET" }),
      Map.entry("/api/reward-policies", new String[] { "GET" }),
      Map.entry("/api/reward-policies/{id}", new String[] { "GET" }),
      Map.entry("/api/reward-issues", new String[] { "GET" }),
      Map.entry("/api/reward-issues/{id}", new String[] { "GET" }),
      Map.entry("/api/event-participations", new String[] { "POST" }));

  public static String[] urlPatternsFor(Map<String, String[]> endpointMap) {
    return endpointMap.keySet().toArray(new String[0]);
  }
}
