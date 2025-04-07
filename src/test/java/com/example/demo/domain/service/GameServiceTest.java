package com.example.demo.domain.service;

import com.example.demo.domain.entity.*;
import com.example.demo.domain.exception.DeckAlreadyInUseException;
import com.example.demo.domain.exception.DeckNotFoundException;
import com.example.demo.domain.exception.GameNotFoundException;
import com.example.demo.domain.service.impl.DeckServiceImpl;
import com.example.demo.domain.service.impl.GameServiceImpl;
import com.example.demo.infrastructure.repository.*;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {GameServiceImpl.class, DeckServiceImpl.class})
class GameServiceTest {

  @MockitoBean
  private GameCardRepository gameCardRepository;
  @MockitoBean
  private PlayerRepository playerRepository;
  @MockitoBean
  private GameRepository gameRepository;
  @MockitoBean
  private DeckRepository deckRepository;

  @Autowired
  private DeckService deckService;
  @Autowired
  private GameService gameService;

  @Test
  void mustCreateGame() {
    Mockito.when(gameRepository.save(Mockito.any())).thenAnswer(params -> params.getArguments()[0]);

    Game startedGame = gameService.startNewGame();

    Assertions.assertThat(startedGame).isNotNull();
  }

  @Test
  void mustRemoveGameWhenItExists() {
    Mockito.when(gameRepository.existsById(1L)).thenReturn(true);
    Mockito.doNothing().when(gameRepository).deleteById(1L);

    gameService.removeGame(1L);

    Mockito.verify(gameRepository, Mockito.times(1)).deleteById(1L);
  }

  @Test
  void mustRemoveGameWhenItDoesNotExists() {
    Mockito.when(gameRepository.existsById(1L)).thenReturn(false);

    Assertions.assertThatThrownBy(() -> gameService.removeGame(1L))
      .isInstanceOf(GameNotFoundException.class)
      .hasMessage("Game not found with id 1.");
  }

  @Test
  void mustShuffleGameDeck() {
    Game game = new Game();
    Player player = new Player("richard");
    game.addPlayer(player);

    Card cardOne = new Card(CardValue.ACE, CardSuit.DIAMONDS, new Deck());
    Card cardTwo = new Card(CardValue.TWO, CardSuit.CLUBS, new Deck());
    Card cardThree = new Card(CardValue.THREE, CardSuit.SPADES, new Deck());
    Card cardFour = new Card(CardValue.FOUR, CardSuit.HEARTS, new Deck());
    Card cardFive = new Card(CardValue.FIVE, CardSuit.HEARTS, new Deck());
    game.addCards(List.of(cardOne, cardTwo, cardThree, cardFour, cardFive));

    List<GameCard> gameCardsBefore = game.getCards();

    Mockito.when(gameRepository.findById(1L)).thenReturn(java.util.Optional.of(game));
    Mockito.when(gameRepository.save(Mockito.any())).thenAnswer(params -> params.getArguments()[0]);

    gameService.shuffleGameDeck(1L);

    List<GameCard> gameCardsAfter = game.getCards();

    Mockito.verify(gameRepository, Mockito.times(1)).save(game);

    Assertions.assertThat(gameCardsBefore).doesNotContainSequence(gameCardsAfter);
  }

  @Test
  void mustAddDeckToGameWhenBothExists() {
    Game game = new Game();

    Mockito.when(deckRepository.save(Mockito.any())).thenAnswer(params -> params.getArguments()[0]);

    Deck deck = deckService.createDeck();

    Mockito.when(gameRepository.findById(1L)).thenReturn(java.util.Optional.of(game));
    Mockito.when(deckRepository.findById(1L)).thenReturn(Optional.of(deck));
    Mockito.when(gameRepository.save(Mockito.any())).thenAnswer(params -> params.getArguments()[0]);

    gameService.addDeckToGame(1L, 1L);

    Mockito.verify(gameRepository, Mockito.times(1)).save(game);

    Assertions.assertThat(game.getCards()).hasSize(52);
  }

  @Test
  void mustThrowExceptionWhenAddingDeckToGameThatDoesNotExists() {
    Mockito.when(gameRepository.findById(1L)).thenReturn(java.util.Optional.empty());

    Assertions.assertThatThrownBy(() -> gameService.addDeckToGame(1L, 1L))
      .isInstanceOf(GameNotFoundException.class)
      .hasMessage("Game not found with id 1.");
  }

  @Test
  void mustThrowExceptionWhenAddingDeckThatDoesNotExists() {
    Game game = new Game();

    Mockito.when(gameRepository.findById(1L)).thenReturn(java.util.Optional.of(game));
    Mockito.when(deckRepository.findById(1L)).thenReturn(Optional.empty());

    Assertions.assertThatThrownBy(() -> gameService.addDeckToGame(1L, 1L))
      .isInstanceOf(DeckNotFoundException.class)
      .hasMessage("Deck not found with id 1.");
  }

  @Test
  void mustThrowExceptionWhenAddingDeckThatIsAlreadyInUse() {
    Game game = new Game();
    Deck deck = new Deck();

    Mockito.when(gameRepository.findById(1L)).thenReturn(java.util.Optional.of(game));
    Mockito.when(deckRepository.findById(1L)).thenReturn(Optional.of(deck));
    Mockito.when(gameCardRepository.someCardIsBeingUsedByDeck(deck.getId())).thenReturn(true);

    Assertions.assertThatThrownBy(() -> gameService.addDeckToGame(1L, 1L))
      .isInstanceOf(DeckAlreadyInUseException.class)
      .hasMessage("Deck with id 1 is already in use.");
  }

  @Test
  void mustRetrievePlayerGameCards() {
    Game game = new Game();
    Player player = new Player("richard");
    player.addCard(new GameCard(new Card(CardValue.ACE, CardSuit.DIAMONDS, new Deck()), game, 1));
    player.addCard(new GameCard(new Card(CardValue.TWO, CardSuit.CLUBS, new Deck()), game, 2));

    Mockito.when(gameRepository.findById(1L)).thenReturn(java.util.Optional.of(game));
    Mockito.when(playerRepository.findByIdAndGameId(5L, 1L)).thenReturn(java.util.Optional.of(player));
    Mockito.when(gameCardRepository.findAllByGameIdAndOwnerId(game.getId(), player.getId()))
      .thenReturn(List.of(
        new GameCard(new Card(CardValue.ACE, CardSuit.DIAMONDS, new Deck()), game, 1),
        new GameCard(new Card(CardValue.TWO, CardSuit.CLUBS, new Deck()), game, 2)
      ));

    List<GameCard> playerGameCards = gameService.retrievePlayerGameCards(1L, 5L);

    Assertions.assertThat(playerGameCards).hasSize(2);
  }

  @Test
  void mustDealCardToPlayer() {
    Game game = new Game();
    Player player = new Player("richard");
    game.addCards(List.of(new Card(CardValue.ACE, CardSuit.DIAMONDS, new Deck())));

    Mockito.when(gameRepository.findById(1L)).thenReturn(java.util.Optional.of(game));
    Mockito.when(playerRepository.findByIdAndGameId(5L, 1L)).thenReturn(java.util.Optional.of(player));
    Mockito.when(gameRepository.save(Mockito.any())).thenAnswer(params -> params.getArguments()[0]);

    GameCard dealtCard = gameService.dealCard(1L, 5L);

    Assertions.assertThat(dealtCard).isNotNull();
    Assertions.assertThat(dealtCard.getOwner()).isEqualTo(player);
    Assertions.assertThat(game.getCards()).doesNotContain(dealtCard);
  }
}