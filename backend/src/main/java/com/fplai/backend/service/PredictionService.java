package com.fplai.backend.service;

import com.fplai.backend.dto.ml.PredictionRequestDto;
import com.fplai.backend.dto.ml.PredictionResponseDto;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class PredictionService {

    private final RestClient mlRestClient;

    public PredictionService(@Qualifier("mlRestClient") RestClient mlRestClient) {
      this.mlRestClient = mlRestClient;
    }

    public PredictionResponseDto getPrediction(PredictionRequestDto request) {
      return mlRestClient.post()
        .uri("/predict")
        .body(request)
        .retrieve()
        .body(PredictionResponseDto.class);
    }
}