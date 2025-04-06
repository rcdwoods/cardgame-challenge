package com.example.demo.infrastructure.resource.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;
import java.util.Set;

public class GameResponse {
  public Long id;
  public GameDeckResponse deck;
  @JsonProperty("created_at")
  public LocalDateTime createdAt;
  public Set<PlayerResponse> players;
}
