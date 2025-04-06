package com.example.demo.infrastructure.resource.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class DeckResponse {
  public Long id;
  @JsonProperty("game_id")
  public Long gameId;
  public List<CardResponse> cards;
}
