package com.example.demo.infrastructure.repository;

import com.example.demo.domain.entity.Game;
import com.example.demo.domain.entity.GameCard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface GameCardRepository extends JpaRepository<GameCard, Long> {
  List<GameCard> findAllByGameDeckIdAndOwnerId(Long gameDeckId, Long ownerId);
}
