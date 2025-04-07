package com.example.demo.domain.entity;

import com.example.demo.domain.exception.InvalidCardNumberException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class CardValueTest {

  @Test
  void mustCreateCardNameFromNumber() {
    assertEquals(CardValue.ACE, CardValue.fromNumber(1));
    assertEquals(CardValue.TWO, CardValue.fromNumber(2));
    assertEquals(CardValue.THREE, CardValue.fromNumber(3));
    assertEquals(CardValue.FOUR, CardValue.fromNumber(4));
    assertEquals(CardValue.FIVE, CardValue.fromNumber(5));
    assertEquals(CardValue.SIX, CardValue.fromNumber(6));
    assertEquals(CardValue.SEVEN, CardValue.fromNumber(7));
    assertEquals(CardValue.EIGHT, CardValue.fromNumber(8));
    assertEquals(CardValue.NINE, CardValue.fromNumber(9));
    assertEquals(CardValue.TEN, CardValue.fromNumber(10));
    assertEquals(CardValue.JACK, CardValue.fromNumber(11));
    assertEquals(CardValue.QUEEN, CardValue.fromNumber(12));
    assertEquals(CardValue.KING, CardValue.fromNumber(13));
  }

  @Test
  void mustGetCardNumbers() {
    assertEquals(1, CardValue.ACE.getNumber());
    assertEquals(2, CardValue.TWO.getNumber());
    assertEquals(3, CardValue.THREE.getNumber());
    assertEquals(4, CardValue.FOUR.getNumber());
    assertEquals(5, CardValue.FIVE.getNumber());
    assertEquals(6, CardValue.SIX.getNumber());
    assertEquals(7, CardValue.SEVEN.getNumber());
    assertEquals(8, CardValue.EIGHT.getNumber());
    assertEquals(9, CardValue.NINE.getNumber());
    assertEquals(10, CardValue.TEN.getNumber());
    assertEquals(11, CardValue.JACK.getNumber());
    assertEquals(12, CardValue.QUEEN.getNumber());
    assertEquals(13, CardValue.KING.getNumber());
  }

  @ParameterizedTest
  @ValueSource(ints = {0, 14, 15})
  void mustThrowExceptionWhenNumberIsNotValid(Integer number) {
    Exception exception = assertThrows(InvalidCardNumberException.class, () -> CardValue.fromNumber(number));

    assertEquals("Invalid card number: " + number, exception.getMessage());
  }
}