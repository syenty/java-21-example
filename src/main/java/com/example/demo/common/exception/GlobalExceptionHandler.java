package com.example.demo.common.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

  /**
   * 비즈니스 예외 처리
   */
  @ExceptionHandler(BusinessException.class)
  public ResponseEntity<ErrorResponse> handleBusinessException(BusinessException ex) {
    ErrorCode errorCode = ex.getErrorCode();

    ErrorResponse body = new ErrorResponse(
        errorCode.getCode(),
        errorCode.getMessage());

    return ResponseEntity
        .status(errorCode.getStatus())
        .body(body);
  }

  /**
   * 그 외 예상 못 한 예외 처리
   */
  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorResponse> handleException(Exception ex) {
    // 로그 남기고
    // log.error("Unexpected error", ex);

    ErrorCode errorCode = ErrorCode.INTERNAL_ERROR;

    ErrorResponse body = new ErrorResponse(
        errorCode.getCode(),
        errorCode.getMessage());

    return ResponseEntity
        .status(errorCode.getStatus())
        .body(body);
  }
}
