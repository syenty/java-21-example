package com.example.demo.common.exception;

import java.util.List;

public class ValidationErrorResponse extends ErrorResponse {

  private final List<ValidationError> errors;

  public ValidationErrorResponse(String code, String message, List<ValidationError> errors) {
    super(code, message);
    this.errors = errors;
  }

  public List<ValidationError> getErrors() {
    return errors;
  }
}
