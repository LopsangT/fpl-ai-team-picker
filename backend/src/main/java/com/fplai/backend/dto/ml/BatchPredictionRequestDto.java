package com.fplai.backend.dto.ml;

import java.util.List;

public class BatchPredictionRequestDto {
    private List<PredictionRequestDto> players;

    public BatchPredictionRequestDto() {}

    public BatchPredictionRequestDto(List<PredictionRequestDto> players) {
        this.players = players;
    }

    public List<PredictionRequestDto> getPlayers() { return players; }
    public void setPlayers(List<PredictionRequestDto> players) { this.players = players; }
}