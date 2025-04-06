package com.example.demo.domain.service.impl;

import com.example.demo.domain.entity.*;
import com.example.demo.domain.exception.DeckAlreadyInUseException;
import com.example.demo.domain.exception.GameNotFoundException;
import com.example.demo.domain.exception.PlayerIsNotInTheGameException;
import com.example.demo.domain.service.DeckService;
import com.example.demo.domain.service.GameService;
import com.example.demo.infrastructure.repository.GameCardRepository;
import com.example.demo.infrastructure.repository.GameDeckRepository;
import com.example.demo.infrastructure.repository.GameRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class GameServiceImpl implements GameService {

  private final GameDeckRepository gameDeckRepository;
  private final GameCardRepository gameCardRepository;
  private final GameRepository gameRepository;
  private final DeckService deckService;

  public GameServiceImpl(
    GameDeckRepository gameDeckRepository,
    GameCardRepository gameCardRepository,
    GameRepository gameRepository,
    DeckService deckService
  ) {
    this.gameCardRepository = gameCardRepository;
    this.gameDeckRepository = gameDeckRepository;
    this.gameRepository = gameRepository;
    this.deckService = deckService;
  }

  @Override
  @Transactional
  public Game startNewGame() {
    GameDeck createdGameDeck = gameDeckRepository.save(new GameDeck());
    Game startedGame = new Game(createdGameDeck);

    return gameRepository.save(startedGame);
  }

  @Override
  @Transactional
  public void removeGame(Long gameId) {
    validateGameExists(gameId);
    gameRepository.deleteById(gameId);
  }

  @Transactional
  public void addPlayerToGame(Long gameId, Player player) {
    Game gameFound = retrieveGame(gameId);
    gameFound.addPlayer(player);
    gameRepository.save(gameFound);
  }

  @Override
  @Transactional
  public void shuffleGameDeck(Long gameId) {
    Game gameFound = gameRepository.findById(gameId)
        .orElseThrow(() -> new GameNotFoundException(gameId));

    gameFound.shuffleDeck();

    gameRepository.save(gameFound);
  }

  @Override
  public Game retrieveGame(Long gameId) {
    return gameRepository.findById(gameId).orElseThrow(() -> new GameNotFoundException(gameId));
  }

  @Override
  public void addDeckToGame(Long gameId, Long deckId) {
    Game gameFound = retrieveGame(gameId);
    Deck deckFound = deckService.retrieveDeck(deckId);

    if (gameCardRepository.someCardIsBeingUsedByDeck(deckFound.getId())) {
      throw new DeckAlreadyInUseException(deckId);
    }

    gameFound.addCards(deckFound.getCards());
    gameRepository.save(gameFound);
  }

  public List<GameCard> retrievePlayerGameCards(Long gameId, Long playerId) {
    Game gameFound = retrieveGame(gameId);
    Player playerFound = gameFound.getPlayers().stream()
        .filter(player -> player.getId().equals(playerId))
        .findFirst()
        .orElseThrow(() -> new PlayerIsNotInTheGameException(playerId));

    return gameCardRepository.findAllByGameDeckIdAndOwnerId(gameFound.getDeck().getId(), playerFound.getId());
  }

  @Override
  public GameCard dealCard(Long gameId, Long playerId) {
    Game gameFound = retrieveGame(gameId);
    Player playerFound = gameFound.getPlayers().stream()
      .filter(player -> player.getId().equals(playerId))
      .findFirst()
      .orElseThrow(() -> new PlayerIsNotInTheGameException(playerId));

    GameCard dealtCard = gameFound.dealCard(playerFound);
    gameRepository.save(gameFound);

    return dealtCard;
  }

  private void validateGameExists(Long gameId) {
    if (!gameRepository.existsById(gameId)) {
      throw new GameNotFoundException(gameId);
    }
  }
}
