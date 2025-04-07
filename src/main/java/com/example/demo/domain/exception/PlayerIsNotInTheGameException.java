package com.example.demo.domain.exception;

public class PlayerIsNotInTheGameException extends BusinessException {
  public PlayerIsNotInTheGameException(Long playerId) {
    super(String.format("Player with ID %s is not in the game.", playerId), "PLAYER_NOT_IN_GAME");
  }
}
