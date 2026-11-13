package com.fplai.backend.dto.team;

import java.util.List;

public class StartingLineupDto {
    private List<SelectedPlayerDto> startingXi;
    private List<SelectedPlayerDto> bench;
    private String formation;

    public StartingLineupDto() {}

    public StartingLineupDto(List<SelectedPlayerDto> startingXi, List<SelectedPlayerDto> bench, String formation) {
        this.startingXi = startingXi;
        this.bench = bench;
        this.formation = formation;
    }

    public List<SelectedPlayerDto> getStartingXi() { return startingXi; }
    public void setStartingXi(List<SelectedPlayerDto> startingXi) { this.startingXi = startingXi; }
    public List<SelectedPlayerDto> getBench() { return bench; }
    public void setBench(List<SelectedPlayerDto> bench) { this.bench = bench; }
    public String getFormation() { return formation; }
    public void setFormation(String formation) { this.formation = formation; }
}
