package com.example.demo.domain.entity;

import com.example.demo.domain.exception.GameHasNoCardsToShuffle;
import com.example.demo.domain.exception.NoCardsLeftInGameDeckException;
import com.example.demo.domain.exception.PlayerAlreadyExistsInTheGameException;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.*;

@Entity
public class Game {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @OneToMany(mappedBy = "game", cascade = CascadeType.ALL, orphanRemoval = true)
  private final Set<Player> players = new HashSet<>();

  @Column(name = "created_at")
  private LocalDateTime createdAt;

  @OneToMany(mappedBy = "game", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<GameCard> cards = new ArrayList<>();

  public Game() {
    this.createdAt = LocalDateTime.now();
  }

  public Long getId() {
    return id;
  }

  public LocalDateTime getCreatedAt() {
    return createdAt;
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
    List<GameCard> undealtCards = getUndealtCards();
    if (undealtCards.isEmpty()) {
      throw new NoCardsLeftInGameDeckException(this.id);
    }

    GameCard card = undealtCards.get(0);
    player.addCard(card);
    return card;
  }

  public void addCards(List<Card> cards) {
    int currentPosition = this.cards.size()+1;

    for (Card card : cards) {
      GameCard gameCard = new GameCard(card, this, currentPosition++);
      this.cards.add(gameCard);
    }
  }

  public List<GameCard> getUndealtCards() {
    List<GameCard> undealtCards = new ArrayList<>();

    for (GameCard gameCard : cards) {
      if (gameCard.isUndealt()) {
        undealtCards.add(gameCard);
      }
    }

    undealtCards.sort(Comparator.comparing(GameCard::getPosition));

    return undealtCards;
  }

  public void shuffleDeck() {
    if (this.getCards().isEmpty()) {
      throw new GameHasNoCardsToShuffle(this.id);
    }

    List<GameCard> undealtCards = getUndealtCards();
    int size = undealtCards.size();

    List<Integer> positions = new ArrayList<>();
    for (int i = 1; i <= size; i++) {
      positions.add(i);
    }

    Random randomGenerator = new Random();

    for (GameCard card : undealtCards) {
      int randomIndex = randomGenerator.nextInt(positions.size());
      int randomPosition = positions.get(randomIndex);
      positions.remove(randomIndex);

      card.setPosition(randomPosition);
    }
  }

  public List<GameCard> getCards() {
    return getUndealtCards();
  }

  public Map<CardSuit, Integer> getUndealtCardsAmountBySuit() {
    Map<CardSuit, Integer> undealtCardsBySuit = new HashMap<>(
      Map.of(
        CardSuit.HEARTS, 0,
        CardSuit.DIAMONDS, 0,
        CardSuit.CLUBS, 0,
        CardSuit.SPADES, 0
      )
    );

    for (GameCard gameCard : getUndealtCards()) {
      CardSuit suit = gameCard.getCard().getSuit();
      Integer currentAmount = undealtCardsBySuit.getOrDefault(suit, 0);
      undealtCardsBySuit.put(suit, currentAmount + 1);
    }

    return undealtCardsBySuit;
  }

  public Map<GameCard, Integer> getUndealtCardsAmountByCardType() {
    Map<GameCard, Integer> remainingCards = new HashMap<>();

    for (GameCard gameCard : cards) {
      Integer currentAmount = remainingCards.getOrDefault(gameCard, 0);
      if (gameCard.isUndealt()) {
        currentAmount++;
      }

      remainingCards.put(gameCard, currentAmount);
    }

    return remainingCards;
  }
}
