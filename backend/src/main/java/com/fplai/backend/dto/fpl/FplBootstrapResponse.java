package com.fplai.backend.dto.fpl;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class FplBootstrapResponse {

    private List<FplTeamDto> teams;
    private List<FplPlayerDto> elements;   
    
    public List<FplTeamDto> getTeams() { return teams; }
    public void setTeams(List<FplTeamDto> teams) { this.teams = teams; }
    public List<FplPlayerDto> getElements() { return elements; }
    public void setElements(List<FplPlayerDto> elements) { this.elements = elements; }
}