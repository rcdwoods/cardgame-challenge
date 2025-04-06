package com.example.demo.infrastructure.resource.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PlayerScoreResponse {
  public Long id;
  public String name;
  public Integer score;
  @JsonProperty("card_amount")
  public Integer cardAmount;
}
