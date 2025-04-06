package com.example.demo.infrastructure.mapper;

import com.example.demo.domain.entity.Player;
import com.example.demo.infrastructure.resource.dto.PlayerRequest;
import com.example.demo.infrastructure.resource.dto.PlayerScoreResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;
import java.util.Set;

@Mapper
public interface PlayerMapper {
  Player toEntity(PlayerRequest playerRequest);
  @Mapping(target = "score", expression = "java(player.getGameScore())")
  PlayerScoreResponse toScoreResponse(Player player);
  List<PlayerScoreResponse> toScoreResponse(Set<Player> players);
}
