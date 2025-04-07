package com.example.demo.infrastructure.mapper;

import com.example.demo.domain.entity.Game;
import com.example.demo.infrastructure.resource.dto.GameResponse;
import org.mapstruct.Mapper;

@Mapper(uses = {CardMapper.class})
public interface GameMapper {
  GameResponse toResponse(Game game);
}
