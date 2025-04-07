package com.example.demo.domain.exception;

public class NoCardsLeftInGameDeckException extends BusinessException {
  public NoCardsLeftInGameDeckException(Long gameId) {
    super(String.format("Game with ID %d has no cards to deal", gameId), "NO_CARDS_LEFT_IN_GAME_DECK");
  }
}
