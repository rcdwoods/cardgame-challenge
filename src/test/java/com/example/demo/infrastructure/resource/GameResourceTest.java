package com.example.demo.infrastructure.resource;

import com.example.demo.domain.entity.CardValue;
import io.restassured.RestAssured;
import io.restassured.response.ValidatableResponse;
import org.assertj.core.api.Assertions;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class GameResourceTest {

  @LocalServerPort
  private int testingPort;

  @BeforeEach
  void setUp() {
    RestAssured.port = testingPort;
  }

  @Test
  void mustStartGame() {
    String date = LocalDate.now().toString();

    RestAssured.given()
      .when()
      .post("/v1/games")
      .then()
      .statusCode(HttpStatus.OK.value())
      .body(
        "id", Matchers.notNullValue(),
        "created_at", Matchers.startsWith(date),
        "players", Matchers.hasSize(0),
        "cards", Matchers.hasSize(0)
      );
  }

  @Test
  void mustDeleteGame() {
    ValidatableResponse creationResponse = RestAssured.given()
      .contentType("application/json")
      .when()
      .post("/v1/games")
      .then()
      .statusCode(HttpStatus.OK.value());

    Integer createdGameId = creationResponse.extract().response().path("id");

    RestAssured.given()
      .contentType("application/json")
      .when()
      .delete(String.format("/v1/games/%s", createdGameId))
      .then()
      .statusCode(HttpStatus.NO_CONTENT.value());
  }

  @Test
  void mustAddDeckToGame() {
    ValidatableResponse gameCreationResponse = RestAssured.given()
      .contentType("application/json")
      .when()
      .post("/v1/games")
      .then()
      .statusCode(HttpStatus.OK.value());

    Integer createdGameId = gameCreationResponse.extract().response().path("id");

    ValidatableResponse deckCreationResponse = RestAssured.given()
      .contentType("application/json")
      .when()
      .post("/v1/decks")
      .then()
      .statusCode(HttpStatus.OK.value());

    Integer createdDeckId = deckCreationResponse.extract().response().path("id");

    RestAssured.given()
      .contentType("application/json")
      .when()
      .post(String.format("/v1/games/%s/decks/%s", createdGameId, createdDeckId))
      .then()
      .statusCode(HttpStatus.NO_CONTENT.value());
  }

  @Test
  void mustAddPlayerToGame() {
    ValidatableResponse gameCreationResponse = RestAssured.given()
      .contentType("application/json")
      .when()
      .post("/v1/games")
      .then()
      .statusCode(HttpStatus.OK.value());

    Integer createdGameId = gameCreationResponse.extract().response().path("id");

    String playerRequest = "{\"name\" : \"richard\"}";

    RestAssured.given()
      .body(playerRequest)
      .contentType("application/json")
      .when()
      .post(String.format("/v1/games/%s/players", createdGameId))
      .then()
      .statusCode(HttpStatus.CREATED.value());
  }

  @Test
  void mustRemovePlayerFromGame() {
    ValidatableResponse gameCreationResponse = RestAssured.given()
      .when()
      .contentType("application/json")
      .post("/v1/games")
      .then()
      .statusCode(HttpStatus.OK.value());

    Integer createdGameId = gameCreationResponse.extract().response().path("id");

    String playerRequest = "{\"name\" : \"richard\"}";

    ValidatableResponse playerCreationResponse = RestAssured.given()
      .body(playerRequest)
      .contentType("application/json")
      .when()
      .post(String.format("/v1/games/%s/players", createdGameId))
      .then()
      .statusCode(HttpStatus.CREATED.value());

    Integer createdPlayerId = playerCreationResponse.extract().response().path("id");

    RestAssured.given()
      .contentType("application/json")
      .when()
      .delete(String.format("/v1/games/%s/players/%s", createdGameId, createdPlayerId))
      .then()
      .statusCode(HttpStatus.NO_CONTENT.value());

    ValidatableResponse playerCardsResponse = RestAssured.given()
      .contentType("application/json")
      .when()
      .get(String.format("/v1/games/%s/players/%s/cards", createdGameId, createdPlayerId))
      .then()
      .statusCode(HttpStatus.BAD_REQUEST.value());

    String errorMessage = playerCardsResponse.extract().response().path("message");

    Assertions.assertThat(errorMessage).isEqualTo(String.format("Player with ID %s is not in the game.", createdPlayerId));
  }

  @Test
  void mustGetPlayerGameCardsWhenItIsEmpty() {
    ValidatableResponse gameCreationResponse = RestAssured.given()
      .when()
      .contentType("application/json")
      .post("/v1/games")
      .then()
      .statusCode(HttpStatus.OK.value());

    Integer createdGameId = gameCreationResponse.extract().response().path("id");

    String playerRequest = "{\"name\" : \"richard\"}";

    ValidatableResponse playerCreationResponse = RestAssured.given()
      .body(playerRequest)
      .contentType("application/json")
      .when()
      .post(String.format("/v1/games/%s/players", createdGameId))
      .then()
      .statusCode(HttpStatus.CREATED.value());

    Integer createdPlayerId = playerCreationResponse.extract().response().path("id");

    RestAssured.given()
      .when()
      .contentType("application/json")
      .get(String.format("/v1/games/%s/players/%s/cards", createdGameId, createdPlayerId))
      .then()
      .statusCode(HttpStatus.OK.value());
  }

  @Test
  void mustGetPlayerGameCardsAfterDealingIt() {
    ValidatableResponse gameCreationResponse = RestAssured.given()
      .when()
      .contentType("application/json")
      .post("/v1/games")
      .then()
      .statusCode(HttpStatus.OK.value());

    Integer createdGameId = gameCreationResponse.extract().response().path("id");

    String playerRequest = "{\"name\" : \"richard\"}";

    ValidatableResponse playerCreationResponse = RestAssured.given()
      .body(playerRequest)
      .contentType("application/json")
      .when()
      .post(String.format("/v1/games/%s/players", createdGameId))
      .then()
      .statusCode(HttpStatus.CREATED.value());

    Integer createdPlayerId = playerCreationResponse.extract().response().path("id");

    ValidatableResponse deckCreationResponse = RestAssured.given()
      .contentType("application/json")
      .when()
      .post("/v1/decks")
      .then()
      .statusCode(HttpStatus.OK.value());

    Integer createdDeckId = deckCreationResponse.extract().response().path("id");

    RestAssured.given()
      .contentType("application/json")
      .when()
      .post(String.format("/v1/games/%s/decks/%s", createdGameId, createdDeckId))
      .then()
      .statusCode(HttpStatus.NO_CONTENT.value());

    ValidatableResponse cardDealtResponse = RestAssured.given()
      .contentType("application/json")
      .when()
      .post(String.format("/v1/games/%s/players/%s/cards-dealing", createdGameId, createdPlayerId))
      .then()
      .statusCode(HttpStatus.OK.value());

    Integer dealtCardId = cardDealtResponse.extract().response().path("id");

    RestAssured.given()
      .contentType("application/json")
      .when()
      .get(String.format("/v1/games/%s/players/%s/cards", createdGameId, createdPlayerId))
      .then()
      .statusCode(HttpStatus.OK.value())
      .body(
        "card_amount", Matchers.equalTo(1),
        "cards", Matchers.hasSize(1),
        "cards[0].id", Matchers.equalTo(dealtCardId),
        "cards[0].value", Matchers.notNullValue(),
        "cards[0].suit", Matchers.notNullValue()
      );
  }

  @Test
  void mustShuffleGameDeck() {
    ValidatableResponse gameCreationResponse = RestAssured.given()
      .when()
      .contentType("application/json")
      .post("/v1/games")
      .then()
      .statusCode(HttpStatus.OK.value());

    Integer createdGameId = gameCreationResponse.extract().response().path("id");

    ValidatableResponse deckCreationResponse = RestAssured.given()
      .contentType("application/json")
      .when()
      .post("/v1/decks")
      .then()
      .statusCode(HttpStatus.OK.value());

    Integer createdDeckId = deckCreationResponse.extract().response().path("id");

    RestAssured.given()
      .contentType("application/json")
      .when()
      .post(String.format("/v1/games/%s/decks/%s", createdGameId, createdDeckId))
      .then()
      .statusCode(HttpStatus.NO_CONTENT.value());

    RestAssured.given()
      .contentType("application/json")
      .when()
      .post(String.format("/v1/games/%s/shuffle", createdGameId))
      .then()
      .statusCode(HttpStatus.NO_CONTENT.value());
  }

  @Test
  void mustRetrieveUndealtCardsSummaryWithAmountOfRemainingCardsBySuit() {
    ValidatableResponse gameCreationResponse = RestAssured.given()
      .contentType("application/json")
      .when()
      .post("/v1/games")
      .then()
      .statusCode(HttpStatus.OK.value());

    Integer createdGameId = gameCreationResponse.extract().response().path("id");

    ValidatableResponse deckCreationResponse = RestAssured.given()
      .contentType("application/json")
      .when()
      .post("/v1/decks")
      .then()
      .statusCode(HttpStatus.OK.value());

    Integer createdDeckId = deckCreationResponse.extract().response().path("id");

    RestAssured.given()
      .contentType("application/json")
      .when()
      .post(String.format("/v1/games/%s/decks/%s", createdGameId, createdDeckId))
      .then()
      .statusCode(HttpStatus.NO_CONTENT.value());

    RestAssured.given()
      .contentType("application/json")
      .when()
      .get(String.format("/v1/games/%s/undealt-cards-summary", createdGameId))
      .then()
      .statusCode(HttpStatus.OK.value())
      .body(
        "hearts", Matchers.equalTo(13),
        "diamonds", Matchers.equalTo(13),
        "clubs", Matchers.equalTo(13),
        "spades", Matchers.equalTo(13)
      );
  }

  @Test
  void mustUpdateUndealtCardsSummaryAfterDealingCard() {
    ValidatableResponse gameCreationResponse = RestAssured.given()
      .contentType("application/json")
      .when()
      .post("/v1/games")
      .then()
      .statusCode(HttpStatus.OK.value());

    Integer createdGameId = gameCreationResponse.extract().response().path("id");

    ValidatableResponse deckCreationResponse = RestAssured.given()
      .contentType("application/json")
      .when()
      .post("/v1/decks")
      .then()
      .statusCode(HttpStatus.OK.value());

    Integer createdDeckId = deckCreationResponse.extract().response().path("id");

    RestAssured.given()
      .contentType("application/json")
      .when()
      .post(String.format("/v1/games/%s/decks/%s", createdGameId, createdDeckId))
      .then()
      .statusCode(HttpStatus.NO_CONTENT.value());

    RestAssured.given()
      .contentType("application/json")
      .when()
      .get(String.format("/v1/games/%s/undealt-cards-summary", createdGameId))
      .then()
      .statusCode(HttpStatus.OK.value())
      .body(
        "hearts", Matchers.equalTo(13),
        "diamonds", Matchers.equalTo(13),
        "clubs", Matchers.equalTo(13),
        "spades", Matchers.equalTo(13)
      );

    String playerRequest = "{\"name\" : \"richard\"}";

    ValidatableResponse playerCreationResponse = RestAssured.given()
      .contentType("application/json")
      .body(playerRequest)
      .when()
      .post(String.format("/v1/games/%s/players", createdGameId))
      .then()
      .statusCode(HttpStatus.CREATED.value());

    Integer createdPlayerId = playerCreationResponse.extract().response().path("id");

    ValidatableResponse cardDealtResponse = RestAssured.given()
      .contentType("application/json")
      .when()
      .post(String.format("/v1/games/%s/players/%s/cards-dealing", createdGameId, createdPlayerId))
      .then()
      .statusCode(HttpStatus.OK.value())
      .body(
        "id", Matchers.notNullValue(),
        "value", Matchers.notNullValue(),
        "suit", Matchers.notNullValue()
      );

    Map<String, Integer> undealtCardsSummary = new HashMap<>(
      Map.of(
        "hearts", 13,
        "diamonds", 13,
        "clubs", 13,
        "spades", 13
      )
    );

    String dealtCardSuit = cardDealtResponse.extract().response().path("suit");

    undealtCardsSummary.put(dealtCardSuit.toLowerCase(), undealtCardsSummary.get(dealtCardSuit.toLowerCase()) - 1);

    RestAssured.given()
      .contentType("application/json")
      .when()
      .get(String.format("/v1/games/%s/undealt-cards-summary", createdGameId))
      .then()
      .statusCode(HttpStatus.OK.value())
      .body(
        "hearts", Matchers.equalTo(undealtCardsSummary.get("hearts")),
        "diamonds", Matchers.equalTo(undealtCardsSummary.get("diamonds")),
        "clubs", Matchers.equalTo(undealtCardsSummary.get("clubs")),
        "spades", Matchers.equalTo(undealtCardsSummary.get("spades"))
      );
  }

  @Test
  void mustRetrieveUndealtCards() {
    ValidatableResponse gameCreationResponse = RestAssured.given()
      .contentType("application/json")
      .when()
      .post("/v1/games")
      .then()
      .statusCode(HttpStatus.OK.value());

    Integer createdGameId = gameCreationResponse.extract().response().path("id");

    ValidatableResponse deckCreationResponse = RestAssured.given()
      .contentType("application/json")
      .when()
      .post("/v1/decks")
      .then()
      .statusCode(HttpStatus.OK.value());

    Integer createdDeckId = deckCreationResponse.extract().response().path("id");

    RestAssured.given()
      .contentType("application/json")
      .when()
      .post(String.format("/v1/games/%s/decks/%s", createdGameId, createdDeckId))
      .then()
      .statusCode(HttpStatus.NO_CONTENT.value());

    RestAssured.given()
      .contentType("application/json")
      .when()
      .get(String.format("/v1/games/%s/undealt-cards", createdGameId))
      .then()
      .statusCode(HttpStatus.OK.value())
      .body(
        "undealt_card_amount", Matchers.equalTo(52),
        "cards", Matchers.hasSize(52),
        "cards[0].value", Matchers.notNullValue(),
        "cards[0].suit", Matchers.notNullValue(),
        "cards[0].count", Matchers.equalTo(1)
      );
  }

  @Test
  void mustRetrieveGameScoresWithMultiplePlayers() {
    ValidatableResponse gameCreationResponse = RestAssured.given()
      .contentType("application/json")
      .when()
      .post("/v1/games")
      .then()
      .statusCode(HttpStatus.OK.value());

    Integer createdGameId = gameCreationResponse.extract().response().path("id");

    String playerRequest = "{\"name\" : \"richard\"}";

    ValidatableResponse deckCreationResponse = RestAssured.given()
      .contentType("application/json")
      .when()
      .post("/v1/decks")
      .then()
      .statusCode(HttpStatus.OK.value());

    Integer createdDeckId = deckCreationResponse.extract().response().path("id");

    RestAssured.given()
      .contentType("application/json")
      .when()
      .post(String.format("/v1/games/%s/decks/%s", createdGameId, createdDeckId))
      .then()
      .statusCode(HttpStatus.NO_CONTENT.value());

    ValidatableResponse playerCreationResponse = RestAssured.given()
      .contentType("application/json")
      .body(playerRequest)
      .when()
      .post(String.format("/v1/games/%s/players", createdGameId))
      .then()
      .statusCode(HttpStatus.CREATED.value());

    Integer createdPlayerId = playerCreationResponse.extract().response().path("id");

    String playerTwoRequest = "{\"name\" : \"john\"}";

    ValidatableResponse playerTwoCreationResponse = RestAssured.given()
      .contentType("application/json")
      .body(playerTwoRequest)
      .when()
      .post(String.format("/v1/games/%s/players", createdGameId))
      .then()
      .statusCode(HttpStatus.CREATED.value());

    playerTwoCreationResponse.extract().response().path("id");

    ValidatableResponse cardDealtResponse = RestAssured.given()
      .contentType("application/json")
      .when()
      .post(String.format("/v1/games/%s/players/%s/cards-dealing", createdGameId, createdPlayerId))
      .then()
      .statusCode(HttpStatus.OK.value());

    String dealtCardSuit = cardDealtResponse.extract().response().path("value");
    CardValue value = CardValue.valueOf(dealtCardSuit.toUpperCase());

    RestAssured.given()
      .contentType("application/json")
      .when()
      .get(String.format("/v1/games/%s/scores", createdGameId))
      .then()
      .statusCode(HttpStatus.OK.value())
      .body(
        "player_amount", Matchers.equalTo(2),
        "player_scores", Matchers.hasSize(2),
        "player_scores[0].name", Matchers.notNullValue(),
        "player_scores[0].score", Matchers.equalTo(value.getNumber()),
        "player_scores[1].name", Matchers.notNullValue(),
        "player_scores[1].score", Matchers.equalTo(0)
      );
  }
}
