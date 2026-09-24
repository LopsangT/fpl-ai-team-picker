package com.fplai.backend.controller;

import com.fplai.backend.dto.ml.PredictionRequestDto;
import com.fplai.backend.dto.ml.PredictionResponseDto;
import com.fplai.backend.service.PredictionService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/predictions")
public class PredictionController {

  private final PredictionService predictionService;

  public PredictionController(PredictionService predictionService) {
    this.predictionService = predictionService;
  }

    // TEMPORARY test endpoint with hardcoded values - proves Spring Boot
    // can successfully call the FastAPI service end-to-end. Will be
    // replaced with a real endpoint that pulls actual player features
    // from the database once player_gameweek_stats has live data.
  @GetMapping("/test")
  public PredictionResponseDto testPrediction() {
    PredictionRequestDto request = new PredictionRequestDto(
      true, 90, 5.0, 1200, "FWD"
    );
    return predictionService.getPrediction(request);
  }
}
