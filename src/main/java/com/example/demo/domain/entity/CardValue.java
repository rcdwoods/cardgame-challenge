package com.example.demo.domain.entity;

import com.example.demo.domain.exception.InvalidCardNumberException;

public enum CardValue {
  ACE(1),
  TWO(2),
  THREE(3),
  FOUR(4),
  FIVE(5),
  SIX(6),
  SEVEN(7),
  EIGHT(8),
  NINE(9),
  TEN(10),
  JACK(11),
  QUEEN(12),
  KING(13);

  private final Integer number;

  CardValue(Integer number) {
    this.number = number;
  }

  public static CardValue fromNumber(Integer number) {
    for (CardValue cardValue : CardValue.values()) {
      if (cardValue.getNumber().equals(number)) {
        return cardValue;
      }
    }

    throw new InvalidCardNumberException(number);
  }

  public Integer getNumber() {
    return number;
  }
}
