package com.fplai.backend.dto.fpl;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

// the FPL API team data is mapped separately from the Team entity so our database
// schema doesn't depend on FPL's exact JSON shape 
@JsonIgnoreProperties(ignoreUnknown = true)
public class FplTeamDto {
  private Integer id;
  private String name;

  @JsonProperty("short_name")
  private String shortName;

  @JsonProperty("strength_overall_home")
  private Integer strengthOverallHome;

  @JsonProperty("strength_overall_away")
  private Integer strengthOverallAway;

  public Integer getId() { return id; }
  public void setId(Integer id) { this.id = id; }
  public String getName() { return name; }
  public void setName(String name) { this.name = name;}
  public String getShortName() { return shortName; }
  public void setShortName(String shortName) { this.shortName = shortName; }
  public Integer getStrengthOverallHome() { return strengthOverallHome; }
  public void setStrengthOverallHome(Integer v) { this.strengthOverallHome = v; }
  public Integer getStrengthOverallAway() { return strengthOverallAway; }
  public void setStrengthOverallAway(Integer v) { this.strengthOverallAway = v;}
}
