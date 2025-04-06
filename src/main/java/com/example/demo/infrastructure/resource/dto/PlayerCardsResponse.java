package com.example.demo.infrastructure.resource.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

public class PlayerCardsResponse {
  @JsonProperty("cards_amount")
  public int cardsAmount;
  public List<GameCardResponse> cards = new ArrayList<>();
}
