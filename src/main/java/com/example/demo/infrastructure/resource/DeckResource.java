package com.example.demo.infrastructure.resource;

import com.example.demo.domain.entity.Deck;
import com.example.demo.domain.service.DeckService;
import com.example.demo.infrastructure.mapper.DeckMapper;
import com.example.demo.infrastructure.resource.dto.DeckResponse;
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

  @PostMapping
  public ResponseEntity<DeckResponse> createDeck() {
    Deck createdDeck = deckService.createDeck();
    return ResponseEntity.ok(deckMapper.toResponse(createdDeck));
  }

  @GetMapping("/{id}")
  public ResponseEntity<DeckResponse> retrieveDeck(@PathVariable Long id) {
    Deck deck = deckService.retrieveDeck(id);
    return ResponseEntity.ok(deckMapper.toResponse(deck));
  }
}
