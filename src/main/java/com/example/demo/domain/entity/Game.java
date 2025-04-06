package com.example.demo.domain.entity;

import com.example.demo.domain.exception.GameHasNoCardsToShuffle;
import com.example.demo.domain.exception.NoCardsLeftInGameDeckException;
import com.example.demo.domain.exception.PlayerAlreadyExistsInTheGameException;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Entity
public class Game {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
  @JoinColumn(name = "deck_id", nullable = false)
  private GameDeck deck;

  @OneToMany(mappedBy = "game", cascade = CascadeType.ALL, orphanRemoval = true)
  private final Set<Player> players = new HashSet<>();

  @Column(name = "created_at")
  private LocalDateTime createdAt;

  public Game() {
    this.createdAt = LocalDateTime.now();
  }

  public Game(GameDeck deck) {
    this.deck = deck;
    this.createdAt = LocalDateTime.now();
  }

  public Long getId() {
    return id;
  }

  public GameDeck getDeck() {
    return deck;
  }

  public LocalDateTime getCreatedAt() {
    return createdAt;
  }

  public void addCards(List<Card> cards) {
    this.deck.addCards(cards);
  }

  public void addPlayer(Player player) {
    if (this.players.contains(player)) {
      throw new PlayerAlreadyExistsInTheGameException(player.getName());
    }

    this.players.add(player);
    player.setGame(this);
  }

  public Set<Player> getPlayers() {
    return players;
  }

  public GameCard dealCard(Player player) {
    List<GameCard> undealtCards = this.deck.getUndealtCards();
    if (undealtCards.isEmpty()) {
      throw new NoCardsLeftInGameDeckException(this.id);
    }

    GameCard card = undealtCards.get(0);
    player.addCard(card);
    return card;
  }

  public void shuffleDeck() {
    if (Objects.isNull(this.deck) || this.deck.getCards().isEmpty()) {
      throw new GameHasNoCardsToShuffle(this.id);
    }

    this.deck.shuffle();
  }
}
