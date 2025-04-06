package com.example.demo.domain.entity;

import jakarta.persistence.*;

import java.util.*;

@Entity
public class GameDeck {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @OneToMany(mappedBy = "gameDeck", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<GameCard> cards = new ArrayList<>();

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

  public void shuffle() {
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

  public Long getId() {
    return id;
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
