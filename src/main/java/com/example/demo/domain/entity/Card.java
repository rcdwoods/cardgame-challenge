package com.example.demo.domain.entity;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
public class Card {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "`value`", nullable = false)
  @Enumerated(EnumType.STRING)
  private CardValue value;

  @Column(nullable = false)
  @Enumerated(EnumType.STRING)
  private CardSuit suit;

  @ManyToOne
  @JoinColumn(name = "deck_id", nullable = false)
  private Deck deck;

  public Card() {
  }

  public Card(CardValue value, CardSuit cardSuit, Deck deck) {
    this.value = value;
    this.suit = cardSuit;
    this.deck = deck;
  }

  public Long getId() {
    return id;
  }

  public CardValue getValue() {
    return value;
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
    return value == card.value && suit == card.suit;
  }

  @Override
  public int hashCode() {
    return Objects.hash(value, suit);
  }
}
