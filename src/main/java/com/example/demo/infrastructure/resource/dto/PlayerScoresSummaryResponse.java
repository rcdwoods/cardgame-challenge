package com.example.demo.infrastructure.resource.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class PlayerScoresSummaryResponse {
  @JsonProperty("player_amount")
  public Long playerAmount;
  @JsonProperty("player_scores")
  public List<PlayerScoreResponse> playerScores;
}
