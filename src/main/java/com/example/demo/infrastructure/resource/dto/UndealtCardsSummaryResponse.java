package com.example.demo.infrastructure.resource.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class UndealtCardsSummaryResponse {
  @JsonProperty("cards_amount")
  public int cardsAmount;
  public List<UndealtCardResponse> cards;
}
