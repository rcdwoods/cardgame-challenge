package com.example.demo.infrastructure.repository;

import com.example.demo.domain.entity.GameDeck;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GameDeckRepository extends JpaRepository<GameDeck, Long> {
  boolean existsById(Long id);
}
