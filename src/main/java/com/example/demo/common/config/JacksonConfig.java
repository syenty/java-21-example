package com.example.demo.common.config;

import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JacksonConfig {

  @Bean
  public Jackson2ObjectMapperBuilderCustomizer jsonCustomizer() {
    return builder -> {
      // Java 8 날짜/시간 모듈 등록
      builder.modules(new JavaTimeModule());

      // LocalDateTime을 배열/숫자 대신 문자열로 직렬화
      builder.featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

      // 기본 날짜 포맷 (원하는 형식으로 변경 가능)
      builder.simpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
    };
  }

}
