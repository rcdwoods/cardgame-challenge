package com.example.demo.domain.service;

import com.example.demo.domain.entity.Game;
import com.example.demo.domain.entity.GameCard;
import com.example.demo.domain.entity.Player;

import java.util.List;

public interface GameService {
  Game startNewGame();
  void removeGame(Long gameId);
  void shuffleGameDeck(Long gameId);
  Game retrieveGame(Long gameId);
  void addDeckToGame(Long gameId, Long deckId);
  void addPlayerToGame(Long gameId, Player player);
  GameCard dealCard(Long gameId, Long playerId);
  List<GameCard> retrievePlayerGameCards(Long gameId, Long playerId);
}
