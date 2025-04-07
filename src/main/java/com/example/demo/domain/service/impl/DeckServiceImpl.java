package com.example.demo.domain.service.impl;

import com.example.demo.domain.entity.Card;
import com.example.demo.domain.entity.CardValue;
import com.example.demo.domain.entity.CardSuit;
import com.example.demo.domain.entity.Deck;
import com.example.demo.domain.exception.DeckNotFoundException;
import com.example.demo.domain.service.DeckService;
import com.example.demo.infrastructure.repository.DeckRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class DeckServiceImpl implements DeckService {

  private final DeckRepository deckRepository;

  public DeckServiceImpl(DeckRepository deckRepository) {
    this.deckRepository = deckRepository;
  }

  @Override
  @Transactional
  public Deck createDeck() {
    Deck createdDeck = deckRepository.save(new Deck());
    createdDeck.setCards(createDeckCards(createdDeck));
    return createdDeck;
  }

  @Override
  public Deck retrieveDeck(Long id) {
    return deckRepository.findById(id)
      .orElseThrow(() -> new DeckNotFoundException(id));
  }

  private List<Card> createDeckCards(Deck deck) {
    List<Card> cards = new ArrayList<>();

    for (CardSuit suit : CardSuit.values()) {
      for (int i = 1; i <= 13; i++) {
        cards.add(new Card(CardValue.fromNumber(i), suit, deck));
      }
    }

    return cards;
  }
}
