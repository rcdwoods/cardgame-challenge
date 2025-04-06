package com.example.demo.domain.exception;

public class NoCardsLeftInDeckException extends BusinessException {
  public NoCardsLeftInDeckException(Long deckId) {
    super("No cards left in deck with id: " + deckId, "NO_CARDS_LEFT_IN_DECK");
  }
}
