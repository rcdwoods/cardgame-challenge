package com.example.demo.infrastructure.resource.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

public class PlayerCardsResponse {
  @JsonProperty("card_amount")
  public int cardAmount;
  public List<GameCardResponse> cards = new ArrayList<>();
}
