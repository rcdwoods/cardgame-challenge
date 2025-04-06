package com.example.demo.infrastructure.mapper;

import com.example.demo.domain.entity.GameCard;
import com.example.demo.infrastructure.resource.dto.GameCardCounterResponse;
import com.example.demo.infrastructure.resource.dto.GameCardResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Mapper
public interface CardMapper {
  @Mapping(source = "card.name", target = "name")
  @Mapping(source = "card.suit", target = "suit")
  GameCardResponse toResponse(GameCard gameCard);
  List<GameCardResponse> toResponse(List<GameCard> gameCards);

  default List<GameCardCounterResponse> toCounterResponse(Map<GameCard, Integer> gameCardCounters) {
    List<GameCardCounterResponse> response = new ArrayList<>();
    for (Map.Entry<GameCard, Integer> entry : gameCardCounters.entrySet()) {
      GameCard gameCard = entry.getKey();
      Integer count = entry.getValue();
      response.add(toCounterResponse(gameCard, count));
    }

    return response;
  }

  default GameCardCounterResponse toCounterResponse(GameCard gameCard, Integer count) {
    GameCardCounterResponse response = new GameCardCounterResponse();
    response.name = gameCard.getCard().getName();
    response.suit = gameCard.getCard().getSuit();
    response.count = count;
    return response;
  }
}
