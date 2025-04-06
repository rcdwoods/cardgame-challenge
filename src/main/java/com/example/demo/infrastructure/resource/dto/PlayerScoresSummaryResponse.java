package com.example.demo.infrastructure.resource.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class PlayerScoresSummaryResponse {
  @JsonProperty("players_amount")
  public Long playersAmount;
  public List<PlayerScoreResponse> scores;
}
