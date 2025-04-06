package com.example.demo.domain.entity;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
public class GameCard {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @OneToOne
  @JoinColumn(name = "card_id", nullable = false)
  private Card card;
  @ManyToOne
  @JoinColumn(name = "game_deck_id", nullable = false)
  private GameDeck gameDeck;
  @ManyToOne
  @JoinColumn(name = "player_id")
  private Player owner;
  private Integer position;

  public GameCard() {
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    GameCard gameCard = (GameCard) o;
    return Objects.equals(card, gameCard.card);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(card);
  }

  public GameCard(Card card, GameDeck gameDeck, Integer position) {
    this.card = card;
    this.gameDeck = gameDeck;
    this.position = position;
  }

  public Long getId() {
    return id;
  }

  public Card getCard() {
    return card;
  }

  public GameDeck getGameDeck() {
    return gameDeck;
  }

  public Integer getPosition() {
    return position;
  }

  public void setPosition(Integer position) {
    this.position = position;
  }

  public Player getOwner() {
    return owner;
  }

  public void setOwner(Player owner) {
    this.owner = owner;
  }

  public boolean isUndealt() {
    return Objects.isNull(owner);
  }

  public int getCardScore() {
    return card.getName().getNumber();
  }
}
