package com.example.demo.domain.entity;

import com.example.demo.domain.exception.BusinessException;

import java.util.Map;

public enum CardName {
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

  CardName(Integer number) {
    this.number = number;
  }

  public static CardName fromNumber(Integer number) {
    for (CardName cardName : CardName.values()) {
      if (cardName.getNumber().equals(number)) {
        return cardName;
      }
    }

    throw new BusinessException(String.format("Invalid card number: %d", number), "INVALID_CARD_NUMBER");
  }

  public Integer getNumber() {
    return number;
  }
}
