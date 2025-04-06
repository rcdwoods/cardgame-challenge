package com.example.demo.domain.exception;

public class PlayerIsNotInTheGameException extends BusinessException {
  public PlayerIsNotInTheGameException(Long playerId) {
    super(String.format("Player with nickname %s is not in the game.", playerId), "PLAYER_NICKNAME_NOT_IN_GAME");
  }
}
