package com.fplai.backend.dto.ml;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PredictionRequestDto {

  @JsonProperty("was_home")
  private boolean wasHome;

  private int minutes;

  @JsonProperty("form_before_gameweek")
  private double formBeforeGameweek;

  @JsonProperty("opponent_difficulty")
  private int opponentDifficulty;

  private String position;

  public PredictionRequestDto() {}

  public PredictionRequestDto(boolean wasHome, int minutes, double formBeforeGameweek,
                                 int opponentDifficulty, String position) {
    this.wasHome = wasHome;
    this.minutes = minutes;
    this.formBeforeGameweek = formBeforeGameweek;
    this.opponentDifficulty = opponentDifficulty;
    this.position = position;
  }

  public boolean isWasHome() { return wasHome; }
  public void setWasHome(boolean wasHome) { this.wasHome = wasHome; }
  public int getMinutes() { return minutes; }
  public void setMinutes(int minutes) { this.minutes = minutes; }
  public double getFormBeforeGameweek() { return formBeforeGameweek; }
  public void setFormBeforeGameweek(double v) { this.formBeforeGameweek = v; }
  public int getOpponentDifficulty() { return opponentDifficulty; }
  public void setOpponentDifficulty(int v) { this.opponentDifficulty = v; }
  public String getPosition() { return position; }
  public void setPosition(String position) { this.position = position; }
}
