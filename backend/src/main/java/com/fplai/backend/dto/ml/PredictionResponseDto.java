package com.fplai.backend.dto.ml;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PredictionResponseDto {

    @JsonProperty("predicted_points")
    private double predictedPoints;

    public double getPredictedPoints() { return predictedPoints; }
    public void setPredictedPoints(double predictedPoints) { this.predictedPoints = predictedPoints; }
}