package com.example.demo.infrastructure.resource.dto;

import com.example.demo.domain.entity.CardSuit;

import java.util.Map;

public class GameCardsSummaryResponse {
  public Integer total;
  public Integer hearts;
  public Integer diamonds;
  public Integer clubs;
  public Integer spades;

  public GameCardsSummaryResponse(Map<CardSuit, Integer> undealtCardsBySuit) {
    this.hearts = undealtCardsBySuit.getOrDefault(CardSuit.HEARTS, 0);
    this.diamonds = undealtCardsBySuit.getOrDefault(CardSuit.DIAMONDS, 0);
    this.clubs = undealtCardsBySuit.getOrDefault(CardSuit.CLUBS, 0);
    this.spades = undealtCardsBySuit.getOrDefault(CardSuit.SPADES, 0);
    this.total = hearts + diamonds + clubs + spades;
  }
}
