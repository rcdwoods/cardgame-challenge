package com.example.demo.infrastructure.repository;

import com.example.demo.domain.entity.Deck;
import com.example.demo.domain.entity.Player;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PlayerRepository extends JpaRepository<Player, Long> {
  Optional<Player> findByIdAndGameId(Long playerId, Long gameId);
}
