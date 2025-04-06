package com.example.demo.domain.exception;

public class PlayerAlreadyExistsInTheGameException extends BusinessException {
  public PlayerAlreadyExistsInTheGameException(String nickname) {
    super(String.format("Player with nickname %s already connected to the game.", nickname), "PLAYER_NICKNAME_ALREADY_EXISTS_IN_GAME");
  }
}
