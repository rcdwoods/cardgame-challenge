package com.example.demo.domain.exception;

public class GameNotFoundException extends EntityNotFoundException {
  public GameNotFoundException(Long id) {
    super(String.format("Game not found with id %s.", id), "GAME_NOT_FOUND");
  }
}
