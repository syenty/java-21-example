package com.example.demo.common.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

  INTERNAL_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "C999", "서버 에러가 발생했습니다.");

  private final HttpStatus status;
  private final String code;
  private final String message;

}
