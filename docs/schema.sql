-- =====================================
-- FPL AI Team Picker - Database Schema
-- =====================================

-- Stores the current premier league clubs and only the fields the app actually uses.
CREATE Table teams (
  id                      INTEGER PRIMARY KEY,
  name                    VARCHAR(50) NOT NULL,
  short_name              VARCHAR(3) NOT NULL,
  strength_overall_home   INTEGER,
  strength_overall_away   INTEGER
);

-- All individual player information will be stored here. 
-- One row per player.
Create TABLE players (
  id                              INTEGER PRIMARY KEY,
  team_id                         INTEGER NOT NULL REFERENCES teams(id),
  first_name                      VARCHAR(50) NOT NULL,
  second_name                     VARCHAR(50) NOT NULL,
  position                        VARCHAR(3) NOT NULL,
  now_cost                        INTEGER NOT NULL,
  status                          VARCHAR(1) NOT NULL,
  chance_of_playing_this_round    INTEGER,
  news                            TEXT
);

-- Every fixture, past and future, across multiple seasons
CREATE TABLE fixtures (
  id                SERIAL PRIMARY KEY,
  season            VARCHAR(9) NOT NULL,
  gameweek          INTEGER NOT NULL,
  home_team_id      INTEGER NOT NULL REFERENCES teams(id),
  away_team_id      INTEGER NOT NULL REFERENCES teams(id),
  home_difficulty   INTEGER,
  away_difficulty   INTEGER,
  kickoff_time      TIMESTAMP,
  finished          BOOLEAN NOT NULL DEFAULT FALSE
);

-- Core training table: one row per player, per gameweek, per season actually played.
-- The ML model will train on this table.
-- total_points is the outcome that the model learns to predict
CREATE TABLE player_gameweek_stats (
  id                      SERIAL PRIMARY KEY,
  player_id               INTEGER NOT NULL REFERENCES players(id),
  season                  VARCHAR(9) NOT NULL,
  gameweek                INTEGER NOT NULL,
  fixture_id              INTEGER REFERENCES fixtures(id),
  opponent_team_id        INTEGER REFERENCES teams(id),
  was_home                BOOLEAN NOT NULL,
  minutes_played          INTEGER NOT NULL,
  goals_scored            INTEGER NOT NULL DEFAULT 0,
  assists                 INTEGER NOT NULL DEFAULT 0,
  clean_sheets            INTEGER NOT NULL DEFAULT 0,
  goals_conceded          INTEGER NOT NULL DEFAULT 0,
  saves                   INTEGER NOT NULL DEFAULT 0,
  bonus                   INTEGER NOT NULL DEFAULT 0,
  form_before_gameweek    NUMERIC(4,2),
  total_points            INTEGER NOT NULL,

  UNIQUE (player_id, season, gameweek)
);

-- ML service provides constant live predictions for upcoming gameweeks in the current season.
-- Refreshed weekly, unlike historical stats which are final.
CREATE TABLE player_predictions (
  id                  SERIAL PRIMARY KEY,
  player_id           INTEGER NOT NULL REFERENCES players(id),
  season              VARCHAR(9) NOT NULL,
  gameweek            INTEGER NOT NULL,
  predicted_points    NUMERIC(5,2) NOT NULL,
  predicted_at        TIMESTAMP NOT NULL DEFAULT NOW(),

  UNIQUE (player_id, season, gameweek)
)
