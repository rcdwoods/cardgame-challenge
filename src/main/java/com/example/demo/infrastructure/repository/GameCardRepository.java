package com.example.demo.infrastructure.repository;

import com.example.demo.domain.entity.GameCard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface GameCardRepository extends JpaRepository<GameCard, Long> {
  List<GameCard> findAllByGameIdAndOwnerId(Long gameDeckId, Long ownerId);
  @Query("SELECT COUNT(g) > 0 FROM GameCard g WHERE g.card.deck.id = ?1")
  boolean someCardIsBeingUsedByDeck(Long cardDeckId);
}
