package com.example.demo.infrastructure.resource;

import com.example.demo.domain.entity.Game;
import com.example.demo.domain.entity.GameCard;
import com.example.demo.domain.entity.Player;
import com.example.demo.domain.service.GameService;
import com.example.demo.domain.service.PlayerService;
import com.example.demo.infrastructure.mapper.CardMapper;
import com.example.demo.infrastructure.mapper.GameMapper;
import com.example.demo.infrastructure.mapper.PlayerMapper;
import com.example.demo.infrastructure.resource.dto.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/v1/games")
public class GameResource {

  private final PlayerService playerService;
  private final GameService gameService;
  private final PlayerMapper playerMapper;
  private final CardMapper gameCardMapper;
  private final GameMapper gameMapper;

  public GameResource(
    PlayerService playerService,
    GameService gameService,
    PlayerMapper playerMapper,
    CardMapper gameCardMapper,
    GameMapper gameMapper
  ) {
    this.gameCardMapper = gameCardMapper;
    this.playerService = playerService;
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
  public ResponseEntity<PlayerResponse> addPlayerToGame(@PathVariable Long gameId, @RequestBody PlayerRequest player) {
    Player playerEntity = playerMapper.toEntity(player);
    Player createdPlayer = playerService.createPlayer(gameId, playerEntity);
    return ResponseEntity.status(HttpStatus.CREATED).body(playerMapper.toResponse(createdPlayer));
  }

  @GetMapping("/{gameId}/players/{playerId}/cards")
  public ResponseEntity<PlayerCardsResponse> retrievePlayerGameCards(
      @PathVariable Long gameId,
      @PathVariable Long playerId
  ) {
    List<GameCard> gameCards = gameService.retrievePlayerGameCards(gameId, playerId);
    return ResponseEntity.ok(gameCardMapper.toPlayerCardsResponse(gameCards));
  }

  @PostMapping("/{gameId}/decks/shuffle")
  public ResponseEntity<Void> shuffleGameDeck(@PathVariable Long gameId) {
    gameService.shuffleGameDeck(gameId);
    return ResponseEntity.noContent().build();
  }

  @PostMapping("/{gameId}/players/{playerId}/cards-dealing")
  public ResponseEntity<GameCardResponse> dealCardToPlayer(@PathVariable Long gameId, @PathVariable Long playerId) {
    GameCard dealtCard = gameService.dealCard(gameId, playerId);
    return ResponseEntity.ok(gameCardMapper.toResponse(dealtCard));
  }

  @GetMapping("/{gameId}/undealt-cards-summary")
  public ResponseEntity<UndealtCardSuitSummaryResponse> retrieveUndealtCards(@PathVariable Long gameId) {
    Game gameFound = gameService.retrieveGame(gameId);
    UndealtCardSuitSummaryResponse cardsSummary = new UndealtCardSuitSummaryResponse(gameFound.getDeck().getUndealtCardsAmountBySuit());
    return ResponseEntity.ok(cardsSummary);
  }

  @GetMapping("/{gameId}/undealt-cards")
  public ResponseEntity<UndealtCardsSummaryResponse> retrieveRemainingCards(@PathVariable Long gameId) {
    Game gameFound = gameService.retrieveGame(gameId);
    Map<GameCard, Integer> remainingCards = gameFound.getDeck().getUndealtCardsAmountByCardType();
    UndealtCardsSummaryResponse undealtCardsSummary = gameCardMapper.toUndealtCardsSummaryResponse(remainingCards);

    return ResponseEntity.ok(undealtCardsSummary);
  }

  @GetMapping("/{gameId}/scores")
  public ResponseEntity<PlayerScoresSummaryResponse> retrieveGameScores(@PathVariable Long gameId) {
    Game gameFound = gameService.retrieveGame(gameId);
    PlayerScoresSummaryResponse playerScores = playerMapper.toSummaryResponse(gameFound.getPlayers());
    return ResponseEntity.ok(playerScores);
  }
}
