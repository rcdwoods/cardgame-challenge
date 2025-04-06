package com.example.demo.domain.exception;

public class InvalidCardNumberException extends BusinessException {
  public InvalidCardNumberException(Integer number) {
    super(String.format("Invalid card number: %d", number), "INVALID_CARD_NUMBER");
  }
}
