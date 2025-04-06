package com.example.demo.infrastructure.mapper;

import com.example.demo.domain.entity.Player;
import com.example.demo.infrastructure.resource.dto.PlayerRequest;
import com.example.demo.infrastructure.resource.dto.PlayerResponse;
import com.example.demo.infrastructure.resource.dto.PlayerScoreResponse;
import com.example.demo.infrastructure.resource.dto.PlayerScoresSummaryResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.Comparator;
import java.util.List;
import java.util.Set;

@Mapper
public interface PlayerMapper {
  Player toEntity(PlayerRequest playerRequest);
  @Mapping(target = "score", expression = "java(player.getGameScore())")
  PlayerScoreResponse toScoreResponse(Player player);
  PlayerResponse toResponse(Player player);
  List<PlayerScoreResponse> toScoreResponse(Set<Player> players);

  default PlayerScoresSummaryResponse toSummaryResponse(Set<Player> players) {
    PlayerScoresSummaryResponse response = new PlayerScoresSummaryResponse();
    List<PlayerScoreResponse> playerScores = toScoreResponse(players);
    playerScores.sort(Comparator.comparing((PlayerScoreResponse player) -> player.score).reversed());

    response.playersAmount = (long) players.size();
    response.scores = playerScores;

    return response;
  }
}
