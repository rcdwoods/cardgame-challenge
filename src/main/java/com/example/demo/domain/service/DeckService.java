package com.example.demo.domain.service;

import com.example.demo.domain.entity.Deck;

public interface DeckService {
  Deck createDeck();
  Deck retrieveDeck(Long id);
}
