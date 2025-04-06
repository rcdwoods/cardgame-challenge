package com.example.demo.domain.exception;

public class GameHasNoDeckToShuffleException extends BusinessException {
  public GameHasNoDeckToShuffleException(Long gameId) {
    super("Game with id " + gameId + " has no deck to shuffle", "GAME_HAS_NO_DECK_TO_SHUFFLE");
  }
}
