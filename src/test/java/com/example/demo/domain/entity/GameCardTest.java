package com.example.demo.domain.entity;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class GameCardTest {

  @Test
  void mustCreateGameCard() {
    Deck deck = new Deck();
    Card card = new Card(CardValue.ACE, CardSuit.HEARTS, deck);
    Game game = new Game();

    GameCard gameCard = new GameCard(card, game, 1);

    Assertions.assertThat(gameCard.getCard()).isEqualTo(card);
    Assertions.assertThat(gameCard.getGame()).isEqualTo(game);
    Assertions.assertThat(gameCard.getPosition()).isEqualTo(1);
  }

  @Test
  void cardMustBeUndealtWhenItDoesNotHaveOwner() {
    Deck deck = new Deck();
    Card card = new Card(CardValue.ACE, CardSuit.HEARTS, deck);
    Game game = new Game();

    GameCard gameCard = new GameCard(card, game, 1);

    Assertions.assertThat(gameCard.getOwner()).isNull();
    Assertions.assertThat(gameCard.isUndealt()).isTrue();
  }

  @Test
  void cardMustBeDealtWhenItHasOwner() {
    Deck deck = new Deck();
    Card card = new Card(CardValue.ACE, CardSuit.HEARTS, deck);
    Game game = new Game();
    Player player = new Player("Player 1");

    GameCard gameCard = new GameCard(card, game, 1);
    gameCard.setOwner(player);

    Assertions.assertThat(gameCard.getOwner()).isEqualTo(player);
    Assertions.assertThat(gameCard.isUndealt()).isFalse();
  }

  @Test
  void mustGetCardScoreBasedOnCardName() {
    Deck deck = new Deck();
    Card card = new Card(CardValue.KING, CardSuit.HEARTS, deck);
    Game game = new Game();
    Player player = new Player("Player 1");

    GameCard gameCard = new GameCard(card, game, 1);
    gameCard.setOwner(player);

    Assertions.assertThat(gameCard.getCardScore()).isEqualTo(13);
  }
}