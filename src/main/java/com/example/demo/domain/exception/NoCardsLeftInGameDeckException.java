package com.example.demo.domain.exception;

public class NoCardsLeftInGameDeckException extends BusinessException {
  public NoCardsLeftInGameDeckException(Long deckId) {
    super(String.format("Game deck with ID %d has no cards to deal", deckId), "NO_CARDS_LEFT_IN_GAME_DECK");
  }
}
