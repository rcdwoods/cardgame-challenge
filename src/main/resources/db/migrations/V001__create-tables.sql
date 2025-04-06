CREATE TABLE `deck`
(
    `id`         bigint NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `created_at` datetime(6) DEFAULT NULL
);

CREATE TABLE `card`
(
    `id`      bigint NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `name`    VARCHAR(20) NOT NULL,
    `suit`    VARCHAR(20) NOT NULL,
    `deck_id` bigint NOT NULL,
    KEY       `idx_card_deck_id` (`deck_id`),
    CONSTRAINT `idx_card_deck_id` FOREIGN KEY (`deck_id`) REFERENCES `deck` (`id`)
);

CREATE TABLE `game_deck`
(
    `id` bigint NOT NULL AUTO_INCREMENT PRIMARY KEY
);

CREATE TABLE `game`
(
    `id`         BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `created_at` DATETIME(6) DEFAULT NULL,
    `deck_id`    BIGINT NOT NULL,
    UNIQUE KEY `uk_game_deck_id` (`deck_id`),
    CONSTRAINT `fk_game_deck_id` FOREIGN KEY (`deck_id`) REFERENCES `game_deck` (`id`)
);

CREATE TABLE `player`
(
    `id`         BIGINT       NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `name`       VARCHAR(255) NOT NULL,
    `game_id`    BIGINT       NOT NULL,
    `created_at` DATETIME(6) DEFAULT NULL,
    KEY          `idx_player_game_id` (`game_id`),
    CONSTRAINT `fk_player_game_id` FOREIGN KEY (`game_id`) REFERENCES `game` (`id`)
);

CREATE TABLE `game_card`
(
    `id`           BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `position`     INT    DEFAULT NULL,
    `card_id`      BIGINT NOT NULL,
    `game_deck_id` BIGINT NOT NULL,
    `player_id`    BIGINT DEFAULT NULL,
    UNIQUE KEY `uk_game_card_card_id` (`card_id`),
    KEY            `idx_game_card_game_deck_id` (`game_deck_id`),
    KEY            `idx_game_card_player_id` (`player_id`),
    CONSTRAINT `fk_game_card_game_deck_id` FOREIGN KEY (`game_deck_id`) REFERENCES `game_deck` (`id`),
    CONSTRAINT `fk_game_card_card_id` FOREIGN KEY (`card_id`) REFERENCES `card` (`id`),
    CONSTRAINT `fk_game_card_player_id` FOREIGN KEY (`player_id`) REFERENCES `player` (`id`)
);
