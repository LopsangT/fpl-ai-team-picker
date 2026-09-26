package com.fplai.backend.dto.fpl;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class FplLiveStatsDto {
  private Integer minutes;
  private Integer assists;
  private Integer saves;
  private Integer bonus;
  
  @JsonProperty("goals_scored")
  private Integer goalsScored;

  @JsonProperty("goals_conceded")
  private Integer goalsConceded;

  @JsonProperty("clean_Sheets")
  private Integer cleanSheets;

  @JsonProperty("totalPoints")
  private Integer totalPoints;

  public Integer getMinutes() { return minutes; }
  public void setMinutes(Integer minutes) { this.minutes = minutes; }
  public Integer getGoalsScored() { return goalsScored; }
  public void setGoalsScored(Integer goalsScored) { this.goalsScored = goalsScored; }
  public Integer getAssists() { return assists; }
  public void setAssists(Integer assists) { this.assists = assists; }
  public Integer getCleanSheets() { return cleanSheets; }
  public void setCleanSheets(Integer cleanSheets) { this.cleanSheets = cleanSheets; }
  public Integer getGoalsConceded() { return goalsConceded; }
  public void setGoalsConceded(Integer goalsConceded) { this.goalsConceded = goalsConceded; }
  public Integer getSaves() { return saves; }
  public void setSaves(Integer saves) { this.saves = saves; }
  public Integer getBonus() { return bonus; }
  public void setBonus(Integer bonus) { this.bonus = bonus; }
  public Integer getTotalPoints() { return totalPoints; }
  public void setTotalPoints(Integer totalPoints) { this.totalPoints = totalPoints; }

}
