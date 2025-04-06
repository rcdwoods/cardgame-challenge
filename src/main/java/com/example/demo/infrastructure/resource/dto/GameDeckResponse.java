package com.example.demo.infrastructure.resource.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class GameDeckResponse {
  public Long id;
  public List<GameCardResponse> cards;
}
