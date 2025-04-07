package com.example.demo.infrastructure.mapper;

import com.example.demo.domain.entity.Deck;
import com.example.demo.infrastructure.resource.dto.DeckResponse;
import com.example.demo.infrastructure.resource.dto.GameDeckResponse;
import org.mapstruct.Mapper;

@Mapper
public interface DeckMapper {
  DeckResponse toResponse(Deck deck);
}
