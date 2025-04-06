package com.example.demo.domain.entity;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
public class Player {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String name;

  @ManyToOne
  @JoinColumn(name = "game_id", nullable = false)
  private Game game;

  @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL, orphanRemoval = true)
  private final List<GameCard> gameCards = new ArrayList<>();

  public Player() {
  }

  public Player(String name) {
    this.name = name;
  }

  public Long getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Game getGame() {
    return game;
  }

  public void setGame(Game game) {
    this.game = game;
  }

  public List<GameCard> getGameCards() {
    return gameCards;
  }

  public void addCard(GameCard gameCard) {
    this.gameCards.add(gameCard);
    gameCard.setOwner(this);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Player player = (Player) o;
    return Objects.equals(name, player.name);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(name);
  }

  public int getGameScore() {
    return gameCards.stream()
        .mapToInt(GameCard::getCardScore)
        .sum();
  }
}
