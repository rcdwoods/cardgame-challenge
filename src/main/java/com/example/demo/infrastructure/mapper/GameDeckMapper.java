package com.example.demo.infrastructure.mapper;

import com.example.demo.domain.entity.GameDeck;
import com.example.demo.infrastructure.resource.dto.GameDeckResponse;
import org.mapstruct.Mapper;

@Mapper(uses = {CardMapper.class})
public interface GameDeckMapper {
  GameDeckResponse toResponse(GameDeck gameDeck);
}
