package com.example.demo.domain.exception;

public class BusinessException extends RuntimeException {
  private final String code;

  public BusinessException(String message, String code) {
    super(message);
    this.code = code;
  }

  public String getCode() {
    return code;
  }
}
