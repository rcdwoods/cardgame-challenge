package com.example.demo.infrastructure.mapper;

import com.example.demo.domain.entity.GameCard;
import com.example.demo.infrastructure.resource.dto.UndealtCardResponse;
import com.example.demo.infrastructure.resource.dto.UndealtCardsSummaryResponse;
import com.example.demo.infrastructure.resource.dto.GameCardResponse;
import com.example.demo.infrastructure.resource.dto.PlayerCardsResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

@Mapper
public interface CardMapper {
  @Mapping(source = "card.value", target = "value")
  @Mapping(source = "card.suit", target = "suit")
  GameCardResponse toResponse(GameCard gameCard);
  List<GameCardResponse> toResponse(List<GameCard> gameCards);

  default PlayerCardsResponse toPlayerCardsResponse(List<GameCard> gameCards) {
    PlayerCardsResponse response = new PlayerCardsResponse();
    response.cardAmount = gameCards.size();
    response.cards = toResponse(gameCards);;
    return response;
  }

  default UndealtCardsSummaryResponse toUndealtCardsSummaryResponse(Map<GameCard, Integer> gameCardCounters) {
    UndealtCardsSummaryResponse summaryResponse = new UndealtCardsSummaryResponse();
    List<UndealtCardResponse> cards = new ArrayList<>();

    for (Map.Entry<GameCard, Integer> entry : gameCardCounters.entrySet()) {
      GameCard gameCard = entry.getKey();
      Integer count = entry.getValue();
      cards.add(toUndealtCardSummaryResponse(gameCard, count));
    }

    cards.sort(Comparator.comparing((UndealtCardResponse card) -> card.suit).thenComparing(card -> card.value.getNumber()).reversed());

    summaryResponse.undealtCardsAmount = cards.stream().mapToInt(undealtCardResponse -> undealtCardResponse.count).sum();
    summaryResponse.cards = cards;

    return summaryResponse;
  }

  default UndealtCardResponse toUndealtCardSummaryResponse(GameCard gameCard, Integer count) {
    UndealtCardResponse response = new UndealtCardResponse();
    response.value = gameCard.getCard().getValue();
    response.suit = gameCard.getCard().getSuit();
    response.count = count;
    return response;
  }
}
