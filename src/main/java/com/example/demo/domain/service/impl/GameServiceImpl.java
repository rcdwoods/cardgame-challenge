package com.example.demo.domain.service.impl;

import com.example.demo.domain.entity.*;
import com.example.demo.domain.exception.DeckAlreadyInUseException;
import com.example.demo.domain.exception.GameNotFoundException;
import com.example.demo.domain.exception.PlayerIsNotInTheGameException;
import com.example.demo.domain.service.DeckService;
import com.example.demo.domain.service.GameService;
import com.example.demo.infrastructure.repository.GameCardRepository;
import com.example.demo.infrastructure.repository.GameRepository;
import com.example.demo.infrastructure.repository.PlayerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class GameServiceImpl implements GameService {

  private final GameCardRepository gameCardRepository;
  private final PlayerRepository playerRepository;
  private final GameRepository gameRepository;
  private final DeckService deckService;

  public GameServiceImpl(
    GameCardRepository gameCardRepository,
    PlayerRepository playerRepository,
    GameRepository gameRepository,
    DeckService deckService
  ) {
    this.gameCardRepository = gameCardRepository;
    this.playerRepository = playerRepository;
    this.gameRepository = gameRepository;
    this.deckService = deckService;
  }

  @Override
  @Transactional
  public Game startNewGame() {
    return gameRepository.save(new Game());
  }

  @Override
  @Transactional
  public void removeGame(Long gameId) {
    validateGameExists(gameId);
    gameRepository.deleteById(gameId);
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
    gameFound.shuffleDeck();
    gameRepository.save(gameFound);
  }

  public List<GameCard> retrievePlayerGameCards(Long gameId, Long playerId) {
    Game gameFound = retrieveGame(gameId);
    Player playerFound = retrievePlayerFromGame(playerId, gameId);

    return gameCardRepository.findAllByGameIdAndOwnerId(gameFound.getId(), playerFound.getId());
  }

  @Override
  public GameCard dealCard(Long gameId, Long playerId) {
    Game gameFound = retrieveGame(gameId);
    Player playerFound = retrievePlayerFromGame(playerId, gameId);

    GameCard dealtCard = gameFound.dealCard(playerFound);
    gameRepository.save(gameFound);

    return dealtCard;
  }

  private Player retrievePlayerFromGame(Long playerId, Long gameId) {
    return playerRepository.findByIdAndGameId(playerId, gameId)
        .orElseThrow(() -> new PlayerIsNotInTheGameException(playerId));
  }

  private void validateGameExists(Long gameId) {
    if (!gameRepository.existsById(gameId)) {
      throw new GameNotFoundException(gameId);
    }
  }
}
