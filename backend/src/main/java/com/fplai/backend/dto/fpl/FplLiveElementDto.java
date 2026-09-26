package com.fplai.backend.dto.fpl;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class FplLiveElementDto {
  private Integer id;
  private FplLiveStatsDto stats;

  public Integer getId() { return id; }
  public void setId(Integer id) { this.id = id; }
  public FplLiveStatsDto getStats() { return stats; }
  public void setStats(FplLiveStatsDto stats) { this.stats = stats; }
}
