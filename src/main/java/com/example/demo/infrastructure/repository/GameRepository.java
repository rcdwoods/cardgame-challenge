package com.example.demo.infrastructure.repository;

import com.example.demo.domain.entity.Game;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GameRepository extends JpaRepository<Game, Long> {
  boolean existsById(Long id);
}
