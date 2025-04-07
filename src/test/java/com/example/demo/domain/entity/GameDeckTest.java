//package com.example.demo.domain.entity;
//
//import org.assertj.core.api.Assertions;
//import org.junit.jupiter.api.Test;
//
//import java.util.List;
//import java.util.Map;
//
//class GameDeckTest {
//
//  @Test
//  void testMustAddCardsToGameDeck() {
//    GameDeck gameDeck = new GameDeck();
//    Card cardOne = new Card(CardName.ACE, CardSuit.HEARTS, null);
//    Card cardTwo = new Card(CardName.KING, CardSuit.SPADES, null);
//    List<Card> cards = List.of(cardOne, cardTwo);
//
//    gameDeck.addCards(cards);
//
//    List<GameCard> gameCards = gameDeck.getUndealtCards();
//
//    Assertions.assertThat(gameCards).hasSize(2);
//    Assertions.assertThat(gameCards).containsExactlyInAnyOrderElementsOf(
//        List.of(
//            new GameCard(cardOne, gameDeck, 1),
//            new GameCard(cardTwo, gameDeck, 2)
//        )
//    );
//  }
//
//  @Test
//  void testMustGetUndealtCardsAmountBySuitWhenDeckIsEmpty() {
//    GameDeck gameDeck = new GameDeck();
//
//    Map<CardSuit, Integer> amountBySuit = gameDeck.getUndealtCardsAmountBySuit();
//
//    Assertions.assertThat(amountBySuit).isEqualTo(Map.of(
//        CardSuit.HEARTS, 0,
//        CardSuit.SPADES, 0,
//        CardSuit.DIAMONDS, 0,
//        CardSuit.CLUBS, 0
//    ));
//  }
//
//  @Test
//  void testMustGetUndealtCardsAmountBySuitWhenDeckHasCards() {
//    GameDeck gameDeck = new GameDeck();
//    Card cardOne = new Card(CardName.ACE, CardSuit.HEARTS, null);
//    Card cardTwo = new Card(CardName.KING, CardSuit.SPADES, null);
//    List<Card> cards = List.of(cardOne, cardTwo);
//
//    gameDeck.addCards(cards);
//
//    Map<CardSuit, Integer> amountBySuit = gameDeck.getUndealtCardsAmountBySuit();
//
//    Assertions.assertThat(amountBySuit).isEqualTo(Map.of(
//        CardSuit.HEARTS, 1,
//        CardSuit.SPADES, 1,
//        CardSuit.DIAMONDS, 0,
//        CardSuit.CLUBS, 0
//    ));
//  }
//
//  @Test
//  void testMustGetUndealtCardsAmountBySuitWhenDeckHasManyCards() {
//    GameDeck gameDeck = new GameDeck();
//    Card cardOne = new Card(CardName.ACE, CardSuit.HEARTS, null);
//    Card cardTwo = new Card(CardName.KING, CardSuit.CLUBS, null);
//    Card cardThree = new Card(CardName.QUEEN, CardSuit.DIAMONDS, null);
//    Card cardFour = new Card(CardName.JACK, CardSuit.DIAMONDS, null);
//    Card cardFive = new Card(CardName.TEN, CardSuit.DIAMONDS, null);
//    Card cardSix = new Card(CardName.NINE, CardSuit.CLUBS, null);
//    Card cardSeven = new Card(CardName.EIGHT, CardSuit.CLUBS, null);
//    Card cardEight = new Card(CardName.SEVEN, CardSuit.HEARTS, null);
//    Card cardNine = new Card(CardName.SIX, CardSuit.CLUBS, null);
//    Card cardTen = new Card(CardName.FIVE, CardSuit.CLUBS, null);
//    List<Card> cards = List.of(cardOne, cardTwo, cardThree, cardFour, cardFive, cardSix, cardSeven, cardEight, cardNine, cardTen);
//
//    gameDeck.addCards(cards);
//
//    Map<CardSuit, Integer> amountBySuit = gameDeck.getUndealtCardsAmountBySuit();
//
//    Assertions.assertThat(amountBySuit).isEqualTo(Map.of(
//        CardSuit.HEARTS, 2,
//        CardSuit.SPADES, 0,
//        CardSuit.DIAMONDS, 3,
//        CardSuit.CLUBS, 5
//    ));
//  }
//
//  @Test
//  void testMustGetUndealtCardsAmountByCardType() {
//    GameDeck gameDeck = new GameDeck();
//    Card cardOne = new Card(CardName.ACE, CardSuit.HEARTS, null);
//    Card cardTwo = new Card(CardName.ACE, CardSuit.HEARTS, null);
//    Card cardThree = new Card(CardName.QUEEN, CardSuit.DIAMONDS, null);
//    Card cardFour = new Card(CardName.ACE, CardSuit.HEARTS, null);
//    Card cardFive = new Card(CardName.TEN, CardSuit.DIAMONDS, null);
//    Card cardSix = new Card(CardName.QUEEN, CardSuit.DIAMONDS, null);
//    Card cardSeven = new Card(CardName.EIGHT, CardSuit.CLUBS, null);
//    Card cardEight = new Card(CardName.EIGHT, CardSuit.CLUBS, null);
//    List<Card> cards = List.of(cardOne, cardTwo, cardThree, cardFour, cardFive, cardSix, cardSeven, cardEight);
//
//    gameDeck.addCards(cards);
//
//    Map<GameCard, Integer> amountByCardType = gameDeck.getUndealtCardsAmountByCardType();
//
//    Assertions.assertThat(amountByCardType).isEqualTo(Map.of(
//        new GameCard(cardOne, gameDeck, 1), 3,
//        new GameCard(cardThree, gameDeck, 3), 2,
//        new GameCard(cardFive, gameDeck, 5), 1,
//        new GameCard(cardSeven, gameDeck, 7), 2
//    ));
//  }
//
//  @Test
//  void testMustGetCardsAmountByCardTypeWithoutCountingThoseThatAreDealt() {
//    GameDeck gameDeck = new GameDeck();
//    Card cardOne = new Card(CardName.ACE, CardSuit.HEARTS, null);
//    Card cardTwo = new Card(CardName.ACE, CardSuit.HEARTS, null);
//    Card cardThree = new Card(CardName.QUEEN, CardSuit.DIAMONDS, null);
//    Card cardFour = new Card(CardName.ACE, CardSuit.HEARTS, null);
//    Card cardFive = new Card(CardName.TEN, CardSuit.DIAMONDS, null);
//    Card cardSix = new Card(CardName.QUEEN, CardSuit.DIAMONDS, null);
//    Card cardSeven = new Card(CardName.EIGHT, CardSuit.CLUBS, null);
//    List<Card> cards = List.of(cardOne, cardTwo, cardThree, cardFour, cardFive, cardSix, cardSeven);
//
//    gameDeck.addCards(cards);
//
//    GameCard heartsAce = gameDeck.getCards().get(0);
//    Player player = new Player("Player 1");
//    player.addCard(heartsAce);
//
//    Map<GameCard, Integer> amountByCardType = gameDeck.getUndealtCardsAmountByCardType();
//
//    Assertions.assertThat(amountByCardType).isEqualTo(Map.of(
//        new GameCard(cardOne, gameDeck, 1), 2,
//        new GameCard(cardThree, gameDeck, 3), 2,
//        new GameCard(cardFive, gameDeck, 5), 1,
//        new GameCard(cardSeven, gameDeck, 7), 1
//    ));
//  }
//}