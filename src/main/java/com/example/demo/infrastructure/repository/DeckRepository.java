package com.example.demo.infrastructure.repository;

import com.example.demo.domain.entity.Deck;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeckRepository extends JpaRepository<Deck, Long> {
}
