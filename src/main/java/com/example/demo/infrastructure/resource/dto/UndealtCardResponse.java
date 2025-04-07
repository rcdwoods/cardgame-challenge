package com.example.demo.infrastructure.resource.dto;

import com.example.demo.domain.entity.CardValue;
import com.example.demo.domain.entity.CardSuit;

public class UndealtCardResponse {
  public CardValue value;
  public CardSuit suit;
  public Integer count;
}
