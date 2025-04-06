package com.example.demo.domain.entity;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

class PlayerTest {

  @Test
  void mustCreatePlayer() {
    Player player = new Player("Player 1");

    assert player.getName().equals("Player 1");
    assert player.getGameCards().isEmpty();
  }

  @Test
  void mustGetPlayerGameScore() {
    Player player = new Player("Player 1");
    GameCard cardOne = new GameCard(new Card(CardName.ACE, CardSuit.HEARTS, null), null, 1);
    GameCard cardTwo = new GameCard(new Card(CardName.KING, CardSuit.SPADES, null), null, 2);
    GameCard cardThree = new GameCard(new Card(CardName.QUEEN, CardSuit.DIAMONDS, null), null, 3);

    player.addCard(cardOne);
    player.addCard(cardTwo);
    player.addCard(cardThree);

    Assertions.assertThat(player.getGameScore()).isEqualTo(26);
  }

  @Test
  void scoreMustBeZeroWhenPlayerHasNoCards() {
    Player player = new Player("Player 1");

    Assertions.assertThat(player.getGameScore()).isEqualTo(0);
  }

  @Test
  void playerMustBeEqualWhenNameIsTheSame() {
    Player playerOne = new Player("richard");
    Player playerTwo = new Player("richard");

    Assertions.assertThat(playerOne).isEqualTo(playerTwo);
  }

  @Test
  void playerMustNotBeEqualWhenNameIsDifferent() {
    Player playerOne = new Player("richard");
    Player playerTwo = new Player("john");

    Assertions.assertThat(playerOne).isNotEqualTo(playerTwo);
  }

  @Test
  void playerMustBeInListWhenNameIsInList() {
    Player playerOne = new Player("richard");
    Player playerTwo = new Player("john");
    Player playerThree = new Player("richard");

    Assertions.assertThat(List.of(playerOne, playerTwo)).contains(playerThree);
  }
}