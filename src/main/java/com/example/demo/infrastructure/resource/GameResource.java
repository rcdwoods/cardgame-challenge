package com.example.demo.infrastructure.resource;

import com.example.demo.domain.entity.Game;
import com.example.demo.domain.entity.GameCard;
import com.example.demo.domain.entity.Player;
import com.example.demo.domain.service.GameService;
import com.example.demo.infrastructure.mapper.CardMapper;
import com.example.demo.infrastructure.mapper.GameMapper;
import com.example.demo.infrastructure.mapper.PlayerMapper;
import com.example.demo.infrastructure.resource.dto.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/v1/games")
public class GameResource {

  private final GameService gameService;
  private final GameMapper gameMapper;
  private final PlayerMapper playerMapper;
  private final CardMapper gameCardMapper;

  public GameResource(
    GameService gameService,
    GameMapper gameMapper,
    PlayerMapper playerMapper,
    CardMapper gameCardMapper
  ) {
    this.gameCardMapper = gameCardMapper;
    this.playerMapper = playerMapper;
    this.gameService = gameService;
    this.gameMapper = gameMapper;
  }

  @PostMapping
  public ResponseEntity<GameResponse> startGame() {
    Game startedGame = gameService.startNewGame();
    return ResponseEntity.ok(gameMapper.toResponse(startedGame));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteGame(@PathVariable Long id) {
    gameService.removeGame(id);
    return ResponseEntity.noContent().build();
  }

  @PostMapping("/{gameId}/decks/{deckId}")
  public ResponseEntity<Void> addDeckToGame(@PathVariable Long gameId, @PathVariable Long deckId) {
    gameService.addDeckToGame(gameId, deckId);
    return ResponseEntity.noContent().build();
  }

  @PostMapping("/{gameId}/players")
  public ResponseEntity<Void> addPlayerToGame(@PathVariable Long gameId, @RequestBody PlayerRequest player) {
    Player playerEntity = playerMapper.toEntity(player);
    gameService.addPlayerToGame(gameId, playerEntity);
    return ResponseEntity.noContent().build();
  }

  @GetMapping("/{gameId}/players/{playerId}/cards")
  public ResponseEntity<List<GameCardResponse>> retrievePlayerGameCards(
      @PathVariable Long gameId,
      @PathVariable Long playerId
  ) {
    List<GameCard> gameCards = gameService.retrievePlayerGameCards(gameId, playerId);
    return ResponseEntity.ok(gameCardMapper.toResponse(gameCards));
  }

  @PostMapping("/{gameId}/shuffle")
  public ResponseEntity<Void> shuffleGameDeck(@PathVariable Long gameId) {
    gameService.shuffleGameDeck(gameId);
    return ResponseEntity.noContent().build();
  }

  @PostMapping("/{gameId}/players/{playerId}/deal")
  public ResponseEntity<GameCardResponse> dealCardToPlayer(@PathVariable Long gameId, @PathVariable Long playerId) {
    GameCard dealtCard = gameService.dealCard(gameId, playerId);
    return ResponseEntity.ok(gameCardMapper.toResponse(dealtCard));
  }

  @GetMapping("/{gameId}/undealt-cards")
  public ResponseEntity<GameCardsSummaryResponse> retrieveUndealtCards(@PathVariable Long gameId) {
    Game gameFound = gameService.retrieveGame(gameId);
    GameCardsSummaryResponse cardsSummary = new GameCardsSummaryResponse(gameFound.getDeck().getUndealtCardsAmountBySuit());
    return ResponseEntity.ok(cardsSummary);
  }

  @GetMapping("/{gameId}/remaining-cards")
  public ResponseEntity<List<GameCardCounterResponse>> retrieveRemainingCards(@PathVariable Long gameId) {
    Game gameFound = gameService.retrieveGame(gameId);
    Map<GameCard, Integer> remainingCards = gameFound.getDeck().getUndealtCardsAmountByCardType();
    List<GameCardCounterResponse> remainingCardsResponse = gameCardMapper.toCounterResponse(remainingCards);
    remainingCardsResponse.sort(Comparator.comparing((GameCardCounterResponse card) -> card.suit)
      .thenComparing(card -> card.name.getNumber()).reversed());

    return ResponseEntity.ok(remainingCardsResponse);
  }

  @GetMapping("/{gameId}/scores")
  public ResponseEntity<List<PlayerScoreResponse>> retrieveGameScores(@PathVariable Long gameId) {
    Game gameFound = gameService.retrieveGame(gameId);
    List<PlayerScoreResponse> playerScores = playerMapper.toScoreResponse(gameFound.getPlayers());
    playerScores.sort(Comparator.comparing((PlayerScoreResponse player) -> player.score).reversed());

    return ResponseEntity.ok(playerScores);
  }
}
