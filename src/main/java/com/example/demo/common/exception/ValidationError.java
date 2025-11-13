package com.example.demo.common.exception;

public record ValidationError(String field, String message) {
}
