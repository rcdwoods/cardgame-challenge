package com.example.demo.infrastructure.resource.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CardResponse {
  public Long id;
  @JsonProperty("game_id")
  public String name;
  public String suit;
}
