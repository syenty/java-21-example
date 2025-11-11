package com.example.demo.common.exception;

import java.time.LocalDateTime;

import lombok.Getter;

@Getter
public class ErrorResponse {

  private final String code;
  private final String message;
  private final LocalDateTime timestamp;

  public ErrorResponse(String code, String message) {
    this.code = code;
    this.message = message;
    this.timestamp = LocalDateTime.now();
  }

}
