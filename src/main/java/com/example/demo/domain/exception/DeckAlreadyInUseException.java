package com.example.demo.domain.exception;

public class DeckAlreadyInUseException extends BusinessException {
  public DeckAlreadyInUseException(Long deckId) {
    super("Deck with id " + deckId + " is already in use.", "DECK_ALREADY_IN_USE");
  }
}
