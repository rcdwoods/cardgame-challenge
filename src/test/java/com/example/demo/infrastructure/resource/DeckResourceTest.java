package com.example.demo.infrastructure.resource;

import io.restassured.RestAssured;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class DeckResourceTest {

  @LocalServerPort
  private int testingPort;

  @Test
  void mustCreateDeck() {
    RestAssured.given().port(testingPort)
      .when()
      .then()
      .statusCode(200)
      .body(
        "id", Matchers.equalTo(1L),
        "cards", Matchers.hasSize(52)
      );
  }
}
