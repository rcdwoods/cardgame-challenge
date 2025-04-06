package com.example.demo.domain.exception;

public class EntityNotFoundException extends BusinessException {
  public EntityNotFoundException(String message, String code) {
    super(message, code);
  }
}
