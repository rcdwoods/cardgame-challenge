package com.example.demo.domain.entity;

import com.example.demo.domain.exception.InvalidCardNumberException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class CardNameTest {

  @Test
  void mustCreateCardNameFromNumber() {
    assertEquals(CardName.ACE, CardName.fromNumber(1));
    assertEquals(CardName.TWO, CardName.fromNumber(2));
    assertEquals(CardName.THREE, CardName.fromNumber(3));
    assertEquals(CardName.FOUR, CardName.fromNumber(4));
    assertEquals(CardName.FIVE, CardName.fromNumber(5));
    assertEquals(CardName.SIX, CardName.fromNumber(6));
    assertEquals(CardName.SEVEN, CardName.fromNumber(7));
    assertEquals(CardName.EIGHT, CardName.fromNumber(8));
    assertEquals(CardName.NINE, CardName.fromNumber(9));
    assertEquals(CardName.TEN, CardName.fromNumber(10));
    assertEquals(CardName.JACK, CardName.fromNumber(11));
    assertEquals(CardName.QUEEN, CardName.fromNumber(12));
    assertEquals(CardName.KING, CardName.fromNumber(13));
  }

  @Test
  void mustGetCardNumbers() {
    assertEquals(1, CardName.ACE.getNumber());
    assertEquals(2, CardName.TWO.getNumber());
    assertEquals(3, CardName.THREE.getNumber());
    assertEquals(4, CardName.FOUR.getNumber());
    assertEquals(5, CardName.FIVE.getNumber());
    assertEquals(6, CardName.SIX.getNumber());
    assertEquals(7, CardName.SEVEN.getNumber());
    assertEquals(8, CardName.EIGHT.getNumber());
    assertEquals(9, CardName.NINE.getNumber());
    assertEquals(10, CardName.TEN.getNumber());
    assertEquals(11, CardName.JACK.getNumber());
    assertEquals(12, CardName.QUEEN.getNumber());
    assertEquals(13, CardName.KING.getNumber());
  }

  @ParameterizedTest
  @ValueSource(ints = {0, 14, 15})
  void mustThrowExceptionWhenNumberIsNotValid(Integer number) {
    Exception exception = assertThrows(InvalidCardNumberException.class, () -> CardName.fromNumber(number));

    assertEquals("Invalid card number: " + number, exception.getMessage());
  }
}