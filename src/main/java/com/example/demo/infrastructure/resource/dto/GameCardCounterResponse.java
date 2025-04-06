package com.example.demo.infrastructure.resource.dto;

import com.example.demo.domain.entity.CardName;
import com.example.demo.domain.entity.CardSuit;

public class GameCardCounterResponse {
  public CardName name;
  public CardSuit suit;
  public Integer count;
}
