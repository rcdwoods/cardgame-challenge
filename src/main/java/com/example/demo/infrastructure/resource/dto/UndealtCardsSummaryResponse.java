package com.example.demo.infrastructure.resource.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class UndealtCardsSummaryResponse {
  @JsonProperty("undealt_card_amount")
  public int undealtCardsAmount;
  public List<UndealtCardResponse> cards;
}
