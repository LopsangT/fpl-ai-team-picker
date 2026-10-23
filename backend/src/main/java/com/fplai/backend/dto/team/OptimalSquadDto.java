package com.fplai.backend.dto.team;

import java.util.List;

public class OptimalSquadDto {
    private List<SelectedPlayerDto> players;
    private int totalCost;
    private double totalPredictedPoints;

    public OptimalSquadDto() {}

    public OptimalSquadDto(List<SelectedPlayerDto> players, int totalCost, double totalPredictedPoints) {
        this.players = players;
        this.totalCost = totalCost;
        this.totalPredictedPoints = totalPredictedPoints;
    }
    
    public List<SelectedPlayerDto> getPlayers() { return players; }
    public void setPlayers(List<SelectedPlayerDto> players) { this.players = players; }
    public int getTotalCost() { return totalCost; }
    public void setTotalCost(int totalCost) { this.totalCost = totalCost; }
    public double getTotalPredictedPoints() { return totalPredictedPoints; }
    public void setTotalPredictedPoints(double totalPredictedPoints) { this.totalPredictedPoints = totalPredictedPoints; }
}
