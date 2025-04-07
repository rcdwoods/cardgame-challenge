package com.example.demo.domain.service;

import com.example.demo.domain.entity.Player;

public interface PlayerService {
  Player createPlayer(Long gameId, Player player);
  void removePlayer(Long gameId, Long playerId);
}
