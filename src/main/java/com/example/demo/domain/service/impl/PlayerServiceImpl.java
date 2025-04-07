package com.example.demo.domain.service.impl;

import com.example.demo.domain.entity.*;
import com.example.demo.domain.exception.PlayerIsNotInTheGameException;
import com.example.demo.domain.service.GameService;
import com.example.demo.domain.service.PlayerService;
import com.example.demo.infrastructure.repository.PlayerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PlayerServiceImpl implements PlayerService {

  private final PlayerRepository playerRepository;
  private final GameService gameService;

  public PlayerServiceImpl(PlayerRepository playerRepository, GameService gameService) {
    this.playerRepository = playerRepository;
    this.gameService = gameService;
  }

  @Transactional
  @Override
  public Player createPlayer(Long gameId, Player player) {
    Game gameFound = gameService.retrieveGame(gameId);
    gameFound.addPlayer(player);

    return playerRepository.save(player);
  }

  @Override
  @Transactional
  public void removePlayer(Long gameId, Long playerId) {
    if (!playerRepository.existsByIdAndGameId(playerId, gameId)) {
      throw new PlayerIsNotInTheGameException(playerId);
    }

    playerRepository.deleteByIdAndGameId(playerId, gameId);
  }
}
