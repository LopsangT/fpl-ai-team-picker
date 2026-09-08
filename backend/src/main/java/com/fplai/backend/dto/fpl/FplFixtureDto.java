package com.fplai.backend.dto.fpl;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class FplFixtureDto {

  private Integer id;

  @JsonProperty("event")
  private Integer gameweek; // FPL calls this "event" 

  @JsonProperty("team_h")
  private Integer teamHome;

  @JsonProperty("team_a")
  private Integer teamAway;

  @JsonProperty("team_h_difficulty")
  private Integer teamHomeDifficulty;

  @JsonProperty("team_a_difficulty")
  private Integer teamAwayDifficulty;

  @JsonProperty("kickoff_time")
  private String kickoffTime;

  private Boolean finished;

  public Integer getId() { return id; }
  public void setId(Integer id) { this.id = id; }
  public Integer getGameweek() { return gameweek; }
  public void setGameweek(Integer gameweek) { this.gameweek = gameweek; }
  public Integer getTeamHome() { return teamHome; }
  public void setTeamHome(Integer teamHome) { this.teamHome = teamHome; }
  public Integer getTeamAway() { return teamAway; }
  public void setTeamAway(Integer teamAway) { this.teamAway = teamAway; }
  public Integer getTeamHomeDifficulty() { return teamHomeDifficulty; }
  public void setTeamHomeDifficulty(Integer v) { this.teamHomeDifficulty = v; }
  public Integer getTeamAwayDifficulty() { return teamAwayDifficulty; }
  public void setTeamAwayDifficulty(Integer v) { this.teamAwayDifficulty = v; }
  public String getKickoffTime() { return kickoffTime; }
  public void setKickoffTime(String kickoffTime) { this.kickoffTime = kickoffTime; }
  public Boolean getFinished() { return finished; }
  public void setFinished(Boolean finished) { this.finished = finished; }
}
