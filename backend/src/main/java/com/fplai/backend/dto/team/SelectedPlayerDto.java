package com.fplai.backend.dto.team;

public class SelectedPlayerDto {
    private Integer playerId;
    private String name;
    private String position;
    private Integer teamId;
    private Integer cost;
    private Double predictedPoints;

    public SelectedPlayerDto() {}

    public SelectedPlayerDto(Integer playerId, String name, String position, Integer teamId, Integer cost, Double predictedPoints) {
        this.playerId = playerId;
        this.name = name;
        this.position = position;
        this.teamId = teamId;
        this.cost = cost;
        this.predictedPoints = predictedPoints;
    }

    public Integer getPlayerId() { return playerId; }
    public void setPlayerId(Integer playerId) { this.playerId = playerId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getPosition() { return position; }
    public void setPosition(String position) { this.position = position; }
    public Integer getTeamId() { return teamId; }
    public void setTeamId(Integer teamId) { this.teamId = teamId; }
    public Integer getCost() { return cost; }
    public void setCost(Integer cost) { this.cost = cost; }
    public Double getPredictedPoints() { return predictedPoints; }
    public void setPredictedPoints(Double predictedPoints) { this.predictedPoints = predictedPoints; }
}