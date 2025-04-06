package com.example.demo.infrastructure.resource;

import com.example.demo.domain.entity.Deck;
import com.example.demo.domain.service.DeckService;
import com.example.demo.infrastructure.mapper.DeckMapper;
import com.example.demo.infrastructure.resource.dto.DeckResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/decks")
public class DeckResource {

  private final DeckService deckService;
  private final DeckMapper deckMapper;

  public DeckResource(DeckMapper deckMapper, DeckService deckService) {
    this.deckService = deckService;
    this.deckMapper = deckMapper;
  }

  @Operation(
    summary = "Create a new deck",
    description = "Creates a new deck and returns its details. This deck can be attached to a game after creation."
  )
  @ApiResponse(responseCode = "200", description = "Deck created successfully")
  @PostMapping(produces = "application/json", consumes = "application/json")
  public ResponseEntity<DeckResponse> createDeck() {
    Deck createdDeck = deckService.createDeck();
    return ResponseEntity.ok(deckMapper.toResponse(createdDeck));
  }
}
