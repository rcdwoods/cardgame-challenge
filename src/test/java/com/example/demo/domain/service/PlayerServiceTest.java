package com.example.demo.domain.service;

import com.example.demo.domain.entity.Game;
import com.example.demo.domain.entity.Player;
import com.example.demo.domain.exception.GameNotFoundException;
import com.example.demo.domain.service.impl.GameServiceImpl;
import com.example.demo.domain.service.impl.PlayerServiceImpl;
import com.example.demo.infrastructure.repository.*;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {PlayerServiceImpl.class, GameServiceImpl.class})
class PlayerServiceTest {

  @MockitoBean
  private PlayerRepository playerRepository;
  @MockitoBean
  private GameCardRepository gameCardRepository;
  @MockitoBean
  private GameRepository gameRepository;
  @MockitoBean
  private DeckService deckService;

  @Autowired
  private PlayerService playerService;
  @Autowired
  private GameService gameService;

  @Test
  void mustAddPlayerToGameWhenGameExists() {
    Game game = new Game();
    Mockito.when(gameRepository.findById(1L)).thenReturn(java.util.Optional.of(game));
    Mockito.when(playerRepository.save(Mockito.any())).thenAnswer(params -> params.getArguments()[0]);

    Player createdPlayer = playerService.createPlayer(1L, new Player("richard"));

    Mockito.verify(playerRepository, Mockito.times(1)).save(createdPlayer);

    Assertions.assertThat(createdPlayer).isNotNull();
  }

  @Test
  void mustThrowExceptionWhenAddingPlayerToGameThatDoesNotExists() {
    Mockito.when(gameRepository.findById(1L)).thenReturn(java.util.Optional.empty());

    Assertions.assertThatThrownBy(() -> playerService.createPlayer(1L, new Player("richard")))
      .isInstanceOf(GameNotFoundException.class)
      .hasMessage("Game not found with id 1.");
  }
}
