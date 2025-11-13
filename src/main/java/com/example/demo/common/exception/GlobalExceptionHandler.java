package com.example.demo.common.exception;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

  /**
   * 비즈니스 예외 처리
   */
  @ExceptionHandler(BusinessException.class)
  public ResponseEntity<ErrorResponse> handleBusinessException(BusinessException ex) {
    return buildResponse(ex.getErrorCode());
  }

  /**
   * 인증 실패 (잘못된 자격 증명)
   */
  @ExceptionHandler(BadCredentialsException.class)
  public ResponseEntity<ErrorResponse> handleBadCredentials(BadCredentialsException ex) {
    return buildResponse(ErrorCode.AUTH_BAD_CREDENTIALS);
  }

  /**
   * 인가 실패 (권한 부족)
   */
  @ExceptionHandler(AccessDeniedException.class)
  public ResponseEntity<ErrorResponse> handleAccessDenied(AccessDeniedException ex) {
    return buildResponse(ErrorCode.AUTH_FORBIDDEN);
  }

  /**
   * 파라미터 검증 실패
   */
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ValidationErrorResponse> handleValidationException(MethodArgumentNotValidException ex) {
    List<ValidationError> errors = ex.getBindingResult()
        .getFieldErrors()
        .stream()
        .map(fieldError -> new ValidationError(fieldError.getField(), fieldError.getDefaultMessage()))
        .collect(Collectors.toList());

    ValidationErrorResponse body = new ValidationErrorResponse(
        ErrorCode.INVALID_PARAMETER.getCode(),
        ErrorCode.INVALID_PARAMETER.getMessage(),
        errors);

    return ResponseEntity
        .status(ErrorCode.INVALID_PARAMETER.getStatus())
        .body(body);
  }

  /**
   * 그 외 예상 못 한 예외 처리
   */
  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorResponse> handleException(Exception ex) {
    log.error("Unexpected error", ex);
    return buildResponse(ErrorCode.INTERNAL_ERROR);
  }

  private ResponseEntity<ErrorResponse> buildResponse(ErrorCode errorCode) {
    ErrorResponse body = new ErrorResponse(
        errorCode.getCode(),
        errorCode.getMessage());

    return ResponseEntity
        .status(errorCode.getStatus())
        .body(body);
  }
}
