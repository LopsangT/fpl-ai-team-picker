package com.fplai.backend.dto.fpl;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

// Maps a single player from FPL's bootstrap-static response
@JsonIgnoreProperties(ignoreUnknown = true)
public class FplPlayerDto {
  
  private Integer id;

  @JsonProperty("first_name")
  private String firstName;

  @JsonProperty("second_name")
  private String secondName;

  private Integer team;

  @JsonProperty("element_type")
  private Integer elementType; // 1 = GKP, 2 = DEF, 3 = MID, 4 = FWD

  @JsonProperty("now_cost")
  private Integer nowCost;

  private String status;
  
  @JsonProperty("chance_of_playing_this_round")
  private Integer chanceOfPlayingThisRound;

  private String news;

  public Integer getId() { return id; }
  public void setId(Integer id) { this.id = id; }
  public String getFirstName() { return firstName; }
  public void setFirstName(String v) { this.firstName = v; }
  public String getSecondName() { return secondName; }
  public void setSecondName(String v) { this.secondName = v; }
  public Integer getTeam() { return team; }
  public void setTeam(Integer team) { this.team = team; }
  public Integer getElementType() { return elementType; }
  public void setElementType(Integer v) { this.elementType = v; }
  public Integer getNowCost() { return nowCost; }
  public void setNowCost(Integer v) { this.nowCost = v; }
  public String getStatus() { return status; }
  public void setStatus(String status) { this.status = status; }
  public Integer getChanceOfPlayingThisRound() { return chanceOfPlayingThisRound; }
  public void setChanceOfPlayingThisRound(Integer v) { this.chanceOfPlayingThisRound = v; }
  public String getNews() { return news; }
  public void setNews(String news) { this.news = news; }

}
