package com.example.demo.domain.entity;

import com.example.demo.domain.exception.GameHasNoCardsToShuffle;
import com.example.demo.domain.exception.NoCardsLeftInGameDeckException;
import com.example.demo.domain.exception.PlayerAlreadyExistsInTheGameException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

class GameTest {

  @Test
  void mustBuildGameWithDeck() {
    GameDeck gameDeck = new GameDeck();
    Game game = new Game(gameDeck);

    Assertions.assertThat(game.getDeck()).isNotNull();
    Assertions.assertThat(game.getDeck()).isEqualTo(gameDeck);
  }

  @Test
  void mustBuildGameWithoutDeck() {
    Game game = new Game();

    Assertions.assertThat(game.getDeck()).isNull();
    Assertions.assertThat(game.getCreatedAt()).isNotNull();
  }

  @Test
  void mustAddCardsToGameAndCalculatePosition() {
    Deck firstDeck = new Deck();

    Game game = new Game(new GameDeck());
    List<Card> firstDeckCards = List.of(
        new Card(CardName.ACE, CardSuit.DIAMONDS, firstDeck),
        new Card(CardName.TWO, CardSuit.CLUBS, firstDeck)
    );
    Deck secondDeck = new Deck();
    List<Card> secondDeckCards = List.of(
      new Card(CardName.THREE, CardSuit.SPADES, secondDeck),
      new Card(CardName.FOUR, CardSuit.HEARTS, secondDeck)
    );


    game.addCards(firstDeckCards);
    game.addCards(secondDeckCards);

    List<GameCard> gameCards = game.getDeck().getCards();

    Assertions.assertThat(gameCards).hasSize(4);
    Assertions.assertThat(gameCards).containsExactlyInAnyOrderElementsOf(List.of(
      new GameCard(firstDeckCards.get(0), game.getDeck(), 1),
      new GameCard(firstDeckCards.get(1), game.getDeck(), 2),
      new GameCard(secondDeckCards.get(0), game.getDeck(), 3),
      new GameCard(secondDeckCards.get(1), game.getDeck(), 4)
    ));

    Assertions.assertThat(gameCards.get(0).getPosition()).isEqualTo(1);
    Assertions.assertThat(gameCards.get(1).getPosition()).isEqualTo(2);
    Assertions.assertThat(gameCards.get(2).getPosition()).isEqualTo(3);
    Assertions.assertThat(gameCards.get(3).getPosition()).isEqualTo(4);
  }

  @Test
  void mustAddPlayerToGame() {
    Game game = new Game();
    Player player = new Player("richard");

    game.addPlayer(player);

    Assertions.assertThat(game.getPlayers()).containsExactly(player);
    Assertions.assertThat(player.getGame()).isEqualTo(game);
  }

  @Test
  void mustThrowExceptionWhenAddingPlayerThatAlreadyExists() {
    Game game = new Game();
    Player player = new Player("richard");

    game.addPlayer(player);

    Assertions.assertThatThrownBy(() -> game.addPlayer(player))
        .isInstanceOf(PlayerAlreadyExistsInTheGameException.class)
        .hasMessage("Player with nickname richard already connected to the game.");
  }

  @Test
  void mustAddMultiplePlayersToGame() {
    Game game = new Game();
    Player player1 = new Player("richard");
    Player player2 = new Player("andre");

    game.addPlayer(player1);
    game.addPlayer(player2);

    Assertions.assertThat(game.getPlayers()).containsExactlyInAnyOrder(player1, player2);
    Assertions.assertThat(player1.getGame()).isEqualTo(game);
    Assertions.assertThat(player2.getGame()).isEqualTo(game);
  }

  @Test
  void mustDealCardToPlayer() {
    GameDeck gameDeck = new GameDeck();
    Game game = new Game(gameDeck);
    Player player = new Player("richard");
    game.addPlayer(player);

    Card card = new Card(CardName.ACE, CardSuit.DIAMONDS, new Deck());
    gameDeck.addCards(List.of(card));

    GameCard dealtCard = game.dealCard(player);

    Assertions.assertThat(dealtCard.getOwner()).isEqualTo(player);
    Assertions.assertThat(dealtCard.getPosition()).isEqualTo(1);
    Assertions.assertThat(player.getGameCards()).containsExactly(dealtCard);
  }

  @Test
  void mustThrowExceptionWhileDealingCardWhenNoCardsLeft() {
    GameDeck gameDeck = new GameDeck();
    Game game = new Game(gameDeck);
    Player player = new Player("richard");
    game.addPlayer(player);

    Assertions.assertThatThrownBy(() -> game.dealCard(player))
        .isInstanceOf(NoCardsLeftInGameDeckException.class)
        .hasMessage("Game deck with ID null has no cards to deal");
  }

  @Test
  void mustThrowExceptionWhileDealingCardWhenAllCardsWereDealt() {
    GameDeck gameDeck = new GameDeck();
    Game game = new Game(gameDeck);
    Player player = new Player("richard");
    game.addPlayer(player);

    Card card = new Card(CardName.ACE, CardSuit.DIAMONDS, new Deck());
    gameDeck.addCards(List.of(card));
    game.dealCard(player);

    Assertions.assertThatThrownBy(() -> game.dealCard(player))
        .isInstanceOf(NoCardsLeftInGameDeckException.class)
        .hasMessage("Game deck with ID null has no cards to deal");
  }

  @Test
  void mustShuffleDeck() {
    GameDeck gameDeck = new GameDeck();
    Game game = new Game(gameDeck);
    Player player = new Player("richard");
    game.addPlayer(player);

    Card cardOne = new Card(CardName.ACE, CardSuit.DIAMONDS, new Deck());
    Card cardTwo = new Card(CardName.TWO, CardSuit.CLUBS, new Deck());
    Card cardThree = new Card(CardName.THREE, CardSuit.SPADES, new Deck());
    Card cardFour = new Card(CardName.FOUR, CardSuit.HEARTS, new Deck());
    Card cardFive = new Card(CardName.FIVE, CardSuit.HEARTS, new Deck());
    gameDeck.addCards(List.of(cardOne, cardTwo, cardThree, cardFour, cardFive));

    List<GameCard> gameCardsBefore = game.getDeck().getCards();

    game.shuffleDeck();

    List<GameCard> gameCardsAfter = game.getDeck().getCards();

    Assertions.assertThat(gameCardsBefore).doesNotContainSequence(gameCardsAfter);
  }

  @Test
  void mustThrowExceptionWhenShufflingDeckWithoutDeck() {
    Game game = new Game();

    Assertions.assertThatThrownBy(game::shuffleDeck)
        .isInstanceOf(GameHasNoCardsToShuffle.class)
        .hasMessage("Game with ID null has no cards to shuffle");
  }
}
