package com.fplai.backend.controller;

import com.fplai.backend.dto.ml.BatchPredictionRequestDto;
import com.fplai.backend.dto.ml.PredictionRequestDto;
import com.fplai.backend.dto.ml.PredictionResponseDto;
import com.fplai.backend.service.PlayerFeatureService;
import com.fplai.backend.service.PredictionService;
import com.fplai.backend.dto.ml.BatchPredictionRequestDto;
import com.fplai.backend.dto.ml.BatchPredictionResponseDto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/api/predictions")
public class PredictionController {

    private final PredictionService predictionService;
    private final PlayerFeatureService playerFeatureService;

    public PredictionController(PredictionService predictionService,
                                PlayerFeatureService playerFeatureService) {
    
        this.predictionService = predictionService;
        this.playerFeatureService = playerFeatureService;
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

    @GetMapping("/player/{playerId}/gameweek/{gameweek}")
    public PredictionResponseDto predictForPlayer(@PathVariable Integer playerId,
                                                @PathVariable int gameweek) {
        PredictionRequestDto features = playerFeatureService.buildFeatures(playerId, gameweek);
        return predictionService.getPrediction(features);
    }

    @GetMapping("batch-test/gameweek/{gameweek}")
    public Map<Integer, Double> batchTest(@PathVariable int gameweek) {
        Map<Integer, PredictionRequestDto> featuresByPlayerId = 
            playerFeatureService.buildFeaturesForAllPlayers(gameweek);

        List<Integer> playerIds = new ArrayList<>(featuresByPlayerId.keySet());
        List<PredictionRequestDto> features = playerIds.stream()  
            .map(featuresByPlayerId::get)
            .toList();
        
        BatchPredictionResponseDto response = 
            predictionService.getBatchPredictions(new BatchPredictionRequestDto(features));
        
        Map<Integer, Double> predictionsByPlayerId = new HashMap<>();
        for (int i = 0; i < playerIds.size(); i++) {
            predictionsByPlayerId.put(playerIds.get(i), response.getPredictions().get(i));
        }
        return predictionsByPlayerId;
    }   
}
