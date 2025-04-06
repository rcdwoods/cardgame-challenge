package com.example.demo.domain.exception;

public class GameHasNoCardsToShuffle extends BusinessException {
  public GameHasNoCardsToShuffle(Long gameId) {
    super(String.format("Game with ID %d has no cards to shuffle", gameId), "GAME_HAS_NO_CARDS_TO_SHUFFLE");
  }
}
