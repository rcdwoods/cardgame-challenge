package com.example.demo.domain.service;

import com.example.demo.domain.entity.Card;
import com.example.demo.domain.entity.CardName;
import com.example.demo.domain.entity.CardSuit;
import com.example.demo.domain.entity.Deck;
import com.example.demo.domain.service.impl.DeckServiceImpl;
import com.example.demo.infrastructure.repository.DeckRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

@ContextConfiguration(classes = DeckServiceImpl.class)
@ExtendWith(SpringExtension.class)
class DeckServiceTest {

  @MockitoBean
  private DeckRepository deckRepository;

  @Autowired
  private DeckService deckService;

  @Test
  void mustCreateDeckWithFiftyTwoStandardCards() {
    Mockito.when(deckRepository.save(Mockito.any(Deck.class))).thenAnswer(params -> params.getArguments()[0]);

    Deck createdDeck = deckService.createDeck();

    Assertions.assertThat(createdDeck).isNotNull();
    Assertions.assertThat(createdDeck.getCards()).hasSize(52);

    Assertions.assertThat(createdDeck.getCards()).filteredOn(card -> card.getName() == CardName.ACE).hasSize(4);
    Assertions.assertThat(createdDeck.getCards()).filteredOn(card -> card.getName() == CardName.TWO).hasSize(4);
    Assertions.assertThat(createdDeck.getCards()).filteredOn(card -> card.getName() == CardName.THREE).hasSize(4);
    Assertions.assertThat(createdDeck.getCards()).filteredOn(card -> card.getName() == CardName.FOUR).hasSize(4);
    Assertions.assertThat(createdDeck.getCards()).filteredOn(card -> card.getName() == CardName.FIVE).hasSize(4);
    Assertions.assertThat(createdDeck.getCards()).filteredOn(card -> card.getName() == CardName.SIX).hasSize(4);
    Assertions.assertThat(createdDeck.getCards()).filteredOn(card -> card.getName() == CardName.SEVEN).hasSize(4);
    Assertions.assertThat(createdDeck.getCards()).filteredOn(card -> card.getName() == CardName.EIGHT).hasSize(4);
    Assertions.assertThat(createdDeck.getCards()).filteredOn(card -> card.getName() == CardName.NINE).hasSize(4);
    Assertions.assertThat(createdDeck.getCards()).filteredOn(card -> card.getName() == CardName.TEN).hasSize(4);
    Assertions.assertThat(createdDeck.getCards()).filteredOn(card -> card.getName() == CardName.JACK).hasSize(4);
    Assertions.assertThat(createdDeck.getCards()).filteredOn(card -> card.getName() == CardName.QUEEN).hasSize(4);
    Assertions.assertThat(createdDeck.getCards()).filteredOn(card -> card.getName() == CardName.KING).hasSize(4);

    Assertions.assertThat(createdDeck.getCards()).filteredOn(card -> card.getSuit() == CardSuit.HEARTS).hasSize(13);
    Assertions.assertThat(createdDeck.getCards()).filteredOn(card -> card.getSuit() == CardSuit.DIAMONDS).hasSize(13);
    Assertions.assertThat(createdDeck.getCards()).filteredOn(card -> card.getSuit() == CardSuit.CLUBS).hasSize(13);
    Assertions.assertThat(createdDeck.getCards()).filteredOn(card -> card.getSuit() == CardSuit.SPADES).hasSize(13);
  }

  @Test
  void mustRetrieveDeck() {
    Deck deck = new Deck();
    deck.setCards(List.of(new Card()));
    Mockito.when(deckRepository.findById(1L)).thenReturn(java.util.Optional.of(deck));

    Deck retrievedDeck = deckService.retrieveDeck(1L);

    Assertions.assertThat(retrievedDeck).isNotNull();
    Assertions.assertThat(retrievedDeck.getCards()).hasSize(1);
  }
}