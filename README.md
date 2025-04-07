# Cardgame API Challenge

## Summary
1. [Description](#description)
2. [Project Execution](#project-execution)
   - [Prerequisites](#prerequisites)
   - [Running the Application and Database with Docker Compose](#running-the-application-and-database-with-docker-compose)
   - [Running only Database with Docker and the Application with Java or a local IDE](#running-only-database-with-docker-and-the-application-with-java-or-a-local-ide)
3. [Swagger API Documentation](#swagger-api-documentation)
4. [Postman Collection](#postman-collection)
5. [Application Operations](#application-operations)
   - [Create a Game](#create-a-game)
   - [Delete a Game](#delete-a-game)
   - [Add An Existent Deck to a Game](#add-an-existent-deck-to-a-game)
   - [Add a Player to a Game](#add-a-player-to-a-game)
   - [Remove a Player from a Game](#remove-a-player-from-a-game)
   - [Deal Cards to Players in a Game](#deal-cards-to-players-in-a-game)
   - [Get the List of Cards for a Player](#get-the-list-of-cards-for-a-player)
   - [Get the List of Players with their Scores](#get-the-list-of-players-with-their-scores)
   - [Get a Summary with the number of undealt cards per suit in a Game](#get-a-summary-with-the-number-of-undealt-cards-per-suit-in-a-game)
   - [Get the List of Undealt Cards in a Game with the Number of each Card in the Game with same Suit and Value](#get-the-list-of-undealt-cards-in-a-game-with-the-number-of-each-card-in-the-game-with-same-suit-and-value)
   - [Shuffle the Game Deck](#shuffle-the-game-deck)

---

## Description

The Cardgame API is a simple RESTful API that allows users to play a card game. The API provides endpoints for creating a 
game, dealing cards, and getting the current state of the game. The game is played with a standard deck of 52 playing cards, and
each game can have multiple decks and players. The API is designed to be easy to use and understand, with clear documentation and examples.

## Project Execution

### Prerequisites
- Java 17 and Docker or only Docker depending on the option you choose to run the application and database.

###
### Running the Application and Database with Docker Compose
In order to run the application and database with Docker Compose, you need to have Docker installed on your machine. Make
sure Docker is running and then run the following commands in the root directory of the project:

```bash
docker-compose up cardgame-db
```

Wait for the database to start up, then run:

```bash
docker-compose up cardgame-api
```


**Warning:** Dont forget to run the database before the application, otherwise the application will not be able to connect to the database. You cannot
run only `docker-compose up` because both will be started at the same time and the application will not be able to connect to the database. 

###
### Running only Database with Docker and the Application with Java or a local IDE
if you want, you can run only the database with Docker and the application with Java or a local IDE. To do this, you need 
to have Docker installed on your machine.

Make sure Docker is running and then run the following command in the root directory of the project:

```bash
docker-compose up cardgame-db
```

Wait for the database to start up, then run the application with Java or your local IDE. To start the application with Java,
you can use the following commands in the root directory of the project:

Build the project with Gradle:
```bash
./gradlew build
```


Run the application:
```bash
java -jar build/libs/cardgame-challenge.jar 
```

###
## Swagger API Documentation
For API documentation, it was used OpenAPI and Swagger UI. You can access the documentation by
navigating to the following URL in your web browser after starting the application:

```
http://localhost:8080/swagger-ui/index.html
```

###
## Postman Collection
You can also import the Postman collection to test the API endpoints. The Postman collection is located in the `postman` folder in
the root directory of the project. You can import the collection by following these steps:

1. Open Postman.
2. Click on the "Import" button in the top left corner.
3. Drag and drop the `cardgame-api.postman_collection.json` file into the import window or click on "Choose Files" and select the file.
5. You can now use the imported collection to test the API endpoints.

###
## Application Operations

The application has the following operations, that can be performed through the API:

#### Create a Game
This operation creates a new game room, with a unique ID and an empty deck of cards. After creating the game, you can add 
decks and players to it.

#### Delete a Game
This operation deletes a game room, removing all its game cards and players.

#### Add An Existent Deck to a Game
This operation adds an existent deck of cards to a game room. The deck is identified by its ID, and the operation will
only succeed if the deck is not already added to a game. The deck is added to the game, and the cards are shuffled.

#### Add a Player to a Game
This operation adds a player to a game room. The player is identified by its ID and nickname, both unique. The player is only added 
to the game if there is no other player with the same nickname. The player is added to the game, and the operation will return the
player's ID and nickname. A player only exists if it is added to a game. If the player is removed from the game, it will be deleted.

#### Remove a Player from a Game
This operation removes a player from a game room. The player is identified by its ID, and the operation will only succeed if the
player is in the game. The player is removed from the game, and the operation will return the player's ID and nickname. If the player
is removed from the game, it will be deleted and all its cards will be returned to the game deck. 

#### Deal Cards to Players in a Game
This operation deals cards to players in a game room. It'll always deal only one card per request and it is required to 
provide the player ID and the game ID. The operation will return the card that was dealt to the player. The card is removed from the
available cards in the game, and the player's hand is updated with the new card. The operation will fail if the player or game ID

#### Get the List of Cards for a Player
This operation gets the list of cards for a player in a game room. The operation will return the list of cards for the player, 
including the card ID, suit, and value, and it will also be returned the number of cards in the player's hand.

#### Get the List of Players with their Scores
This operation gets the list of players in a game room, including their ID, nickname, and score. The score is calculated as the
sum of the values of the cards in the player's hand. The operation will return the list of players with their scores, sorted by score
in descending order. The player with the highest score will be first in the list.

#### Get a Summary with the number of undealt cards per suit in a Game
This operation gets a summary of the number of undealt cards per suit in a game room. The operation will return the number of
undealt cards for each suit, including hearts, diamonds, clubs, and spades. The operation will also return the total number of undealt
cards in the game.

#### Get the List of Undealt Cards in a Game with the Number of each Card in the Game with same Suit and Value
This operation gets the list of undealt cards in a game room, including value, suit and the number of cards for each card. The operation will 
return the list of undealt cards, sorted by suit and value. The operation will also return the total number of undealt cards in the game.
If there are more than one deck added to the game, for exemple, the number of cards for each card will be multiplied by the number of decks.

#### Shuffle the Game Deck
In this operation, the deck of cards is shuffled, what means that the order of the cards is changed randomly. This operation is 
useful when you want to change the order of the cards in the current game. The shuffle operation does not affect the players 
or their scores, only the order of the cards in the deck.