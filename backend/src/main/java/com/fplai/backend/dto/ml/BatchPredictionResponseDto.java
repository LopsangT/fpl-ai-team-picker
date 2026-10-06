package com.fplai.backend.dto.ml;

import java.util.List;

public class BatchPredictionResponseDto {
    private List<Double> predictions;

    public List<Double> getPredictions() { return predictions; }
    public void setPredictions(List<Double> predictions) { this.predictions = predictions; }
}