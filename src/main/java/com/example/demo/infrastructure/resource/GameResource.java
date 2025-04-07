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
import com.example.demo.infrastructure.resource.handler.Problem;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

  @Operation(
    summary = "Create a new game room",
    description = "Create a new game room. The game will be created with an empty deck of cards."
  )
  @ApiResponse(responseCode = "201", description = "Game room created successfully")
  @PostMapping(produces = "application/json")
  public ResponseEntity<GameResponse> startGame() {
    Game startedGame = gameService.startNewGame();
    return ResponseEntity.status(HttpStatus.CREATED).body(gameMapper.toResponse(startedGame));
  }

  @Operation(
    summary = "Delete a game",
    description = "Deletes a game by its ID. This action is irreversible and will remove all associated data."
  )
  @ApiResponse(responseCode = "204", description = "Game deleted successfully")
  @ApiResponse(responseCode = "404", description = "Game Not Found", content = {@Content(schema = @Schema(implementation = Problem.class))})
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteGame(@PathVariable Long id) {
    gameService.removeGame(id);
    return ResponseEntity.noContent().build();
  }

  @Operation(
    summary = "Add a deck to a game",
    description = "Adds a deck to an existing game. The deck must be created first, and all its cards will be added to the game deck."
  )
  @ApiResponse(responseCode = "204", description = "Deck added successfully")
  @ApiResponse(responseCode = "400", description = "Deck already in use", content = {@Content(schema = @Schema(implementation = Problem.class))})
  @ApiResponse(responseCode = "404", description = "Game or Deck Not Found", content = {@Content(schema = @Schema(implementation = Problem.class))})
  @PostMapping(value = "/{gameId}/decks/{deckId}", produces = "application/json")
  public ResponseEntity<Void> addDeckToGame(@PathVariable Long gameId, @PathVariable Long deckId) {
    gameService.addDeckToGame(gameId, deckId);
    return ResponseEntity.noContent().build();
  }

  @Operation(
    summary = "Add a player to a game",
    description = "Adds a player to an existing game. The player will be created and associated with the game."
  )
  @ApiResponse(responseCode = "200", description = "Player added successfully")
  @ApiResponse(responseCode = "400", description = "Player already exists in the game", content = {@Content(schema = @Schema(implementation = Problem.class))})
  @ApiResponse(responseCode = "404", description = "Game Not Found", content = {@Content(schema = @Schema(implementation = Problem.class))})
  @PostMapping(value = "/{gameId}/players", produces = "application/json", consumes = "application/json")
  public ResponseEntity<PlayerResponse> addPlayerToGame(@PathVariable Long gameId, @Valid @RequestBody PlayerRequest player) {
    Player playerEntity = playerMapper.toEntity(player);
    Player createdPlayer = playerService.createPlayer(gameId, playerEntity);
    return ResponseEntity.status(HttpStatus.CREATED).body(playerMapper.toResponse(createdPlayer));
  }

  @Operation(
    summary = "Remove a player from a game",
    description = "Removes a player from an existing game. The player will no longer be associated with the game."
  )
  @ApiResponse(responseCode = "204", description = "Player removed successfully")
  @ApiResponse(responseCode = "400", description = "Player is not part of the game", content = {@Content(schema = @Schema(implementation = Problem.class))})
  @ApiResponse(responseCode = "404", description = "Game or Player Not Found", content = {@Content(schema = @Schema(implementation = Problem.class))})
  @DeleteMapping(value = "/{gameId}/players/{playerId}", produces = "application/json")
  public ResponseEntity<Void> removePlayerFromGame(@PathVariable Long gameId, @PathVariable Long playerId) {
    playerService.removePlayer(gameId, playerId);
    return ResponseEntity.noContent().build();
  }

  @Operation(
    summary = "Retrieve cards of a player in a game",
    description = "Retrieves the cards of a specific player in a game. The player must be part of the game."
  )
  @ApiResponse(responseCode = "200", description = "Player cards retrieved successfully")
  @ApiResponse(responseCode = "400", description = "Player is not part of the game", content = {@Content(schema = @Schema(implementation = Problem.class))})
  @ApiResponse(responseCode = "404", description = "Game or Player Not Found", content = {@Content(schema = @Schema(implementation = Problem.class))})
  @GetMapping(value = "/{gameId}/players/{playerId}/cards", produces = "application/json")
  public ResponseEntity<PlayerCardsResponse> retrievePlayerGameCards(
      @PathVariable Long gameId,
      @PathVariable Long playerId
  ) {
    List<GameCard> gameCards = gameService.retrievePlayerGameCards(gameId, playerId);
    return ResponseEntity.ok(gameCardMapper.toPlayerCardsResponse(gameCards));
  }

  @Operation(
    summary = "Perform a shuffle on the game deck",
    description = "Shuffles the deck of cards in a game. This action will randomize the order of the cards in the game deck."
  )
  @ApiResponse(responseCode = "204", description = "Deck shuffled successfully")
  @ApiResponse(responseCode = "404", description = "Game Not Found", content = {@Content(schema = @Schema(implementation = Problem.class))})
  @PostMapping(value = "/{gameId}/shuffle", produces = "application/json")
  public ResponseEntity<Void> shuffleGameDeck(@PathVariable Long gameId) {
    gameService.shuffleGameDeck(gameId);
    return ResponseEntity.noContent().build();
  }

  @Operation(
    summary = "Deal a card to a player",
    description = "Deals a card to a specific player in a game. The player must be part of the game, and the game must have " +
      "cards available to deal. The dealt card will be removed from available cards in the game deck."
  )
  @ApiResponse(responseCode = "200", description = "Card dealt successfully")
  @ApiResponse(responseCode = "400", description = "No cards available for dealing", content = {@Content(schema = @Schema(implementation = Problem.class))})
  @ApiResponse(responseCode = "404", description = "Game or Player Not Found", content = {@Content(schema = @Schema(implementation = Problem.class))})
  @PostMapping(value = "/{gameId}/players/{playerId}/cards-dealing", produces = "application/json")
  public ResponseEntity<GameCardResponse> dealCardToPlayer(@PathVariable Long gameId, @PathVariable Long playerId) {
    GameCard dealtCard = gameService.dealCard(gameId, playerId);
    return ResponseEntity.ok(gameCardMapper.toResponse(dealtCard));
  }

  @Operation(
    summary = "Retrieve a summary of undealt cards by suit",
    description = "Retrieves a summary of undealt cards by suit in a game. This will show the number of cards available " +
      "for each suit in the game deck."
  )
  @ApiResponse(responseCode = "200", description = "Player scores retrieved successfully")
  @ApiResponse(responseCode = "404", description = "Game Not Found", content = {@Content(schema = @Schema(implementation = Problem.class))})
  @GetMapping(value = "/{gameId}/undealt-cards-summary", produces = "application/json")
  public ResponseEntity<UndealtCardSuitSummaryResponse> retrieveUndealtCards(@PathVariable Long gameId) {
    Game gameFound = gameService.retrieveGame(gameId);
    UndealtCardSuitSummaryResponse cardsSummary = new UndealtCardSuitSummaryResponse(gameFound.getUndealtCardsAmountBySuit());
    return ResponseEntity.ok(cardsSummary);
  }

  @Operation(
    summary = "Retrieve a summary of undealt cards",
    description = "Retrieves a summary of undealt cards in a game. This will show the number of cards available " +
      "for each card type (face value and suit) in the game deck."
  )
  @ApiResponse(responseCode = "200", description = "Player scores retrieved successfully")
  @ApiResponse(responseCode = "404", description = "Game Not Found", content = {@Content(schema = @Schema(implementation = Problem.class))})
  @GetMapping(value = "/{gameId}/undealt-cards", produces = "application/json")
  public ResponseEntity<UndealtCardsSummaryResponse> retrieveRemainingCards(@PathVariable Long gameId) {
    Game gameFound = gameService.retrieveGame(gameId);
    Map<GameCard, Integer> remainingCards = gameFound.getUndealtCardsAmountByCardType();
    UndealtCardsSummaryResponse undealtCardsSummary = gameCardMapper.toUndealtCardsSummaryResponse(remainingCards);

    return ResponseEntity.ok(undealtCardsSummary);
  }

  @Operation(
    summary = "Retrieve a summary of player scores",
    description = "Retrieves a summary of player scores in a game. This will show the players amount, " +
      "the amount of cards each player has, and the total score of each player."
  )
  @ApiResponse(responseCode = "200", description = "Player scores retrieved successfully")
  @ApiResponse(responseCode = "404", description = "Game Not Found", content = {@Content(schema = @Schema(implementation = Problem.class))})
  @GetMapping(value = "/{gameId}/scores", produces = "application/json")
  public ResponseEntity<PlayerScoresSummaryResponse> retrieveGameScores(@PathVariable Long gameId) {
    Game gameFound = gameService.retrieveGame(gameId);
    PlayerScoresSummaryResponse playerScores = playerMapper.toSummaryResponse(gameFound.getPlayers());
    return ResponseEntity.ok(playerScores);
  }
}
