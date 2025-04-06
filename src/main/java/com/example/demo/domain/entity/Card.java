package com.example.demo.domain.entity;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
public class Card {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  @Enumerated(EnumType.STRING)
  private CardName name;

  @Column(nullable = false)
  @Enumerated(EnumType.STRING)
  private CardSuit suit;

  @ManyToOne
  @JoinColumn(name = "deck_id", nullable = false)
  private Deck deck;

  @ManyToOne
  @JoinColumn(name = "game_deck_id")
  private GameDeck gameDeck;

  public Card() {
  }

  public Card(CardName name, CardSuit cardSuit, Deck deck) {
    this.name = name;
    this.suit = cardSuit;
    this.deck = deck;
  }

  public Long getId() {
    return id;
  }

  public CardName getName() {
    return name;
  }

  public CardSuit getSuit() {
    return suit;
  }

  public Deck getDeck() {
    return deck;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Card card = (Card) o;
    return name == card.name && suit == card.suit;
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, suit);
  }

  public GameDeck getGameDeck() {
    return gameDeck;
  }
}
