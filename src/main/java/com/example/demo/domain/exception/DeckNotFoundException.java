package com.example.demo.domain.exception;

public class DeckNotFoundException extends EntityNotFoundException {
  public DeckNotFoundException(Long id) {
    super(String.format("Deck not found with id %s.", id), "DECK_NOT_FOUND");
  }
}
