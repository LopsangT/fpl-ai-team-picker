package com.fplai.backend.service;

import com.fplai.backend.dto.ml.BatchPredictionRequestDto;
import com.fplai.backend.dto.ml.BatchPredictionResponseDto;
import com.fplai.backend.dto.ml.PredictionRequestDto;
import com.fplai.backend.dto.team.OptimalSquadDto;
import com.fplai.backend.dto.team.SelectedPlayerDto;
import com.fplai.backend.entity.Player;
import com.fplai.backend.repository.PlayerRepository;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Selects a 15-player squad that maximises total predicted points
 * within FPL's real constraints: a fixed budget, a fixed formation
 * shape, and a limit of three players per club.
 *
 * Uses a greedy heuristic rather than an exact solver: players are
 * ranked within each position by predicted points per unit cost, and
 * filled into each position slot in that order, skipping any player
 * who would breach the budget or the per-club limit.
 */
@Service
public class TeamOptimiserService {

    private static final int BUDGET = 1000; 
    private static final int MAX_PER_CLUB = 3;

    private static final Map<String, Integer> SQUAD_SHAPE = Map.of(
            "GKP", 2, "DEF", 5, "MID", 5, "FWD", 3
    );

    private final PlayerRepository playerRepository;
    private final PlayerFeatureService playerFeatureService;
    private final PredictionService predictionService;

    public TeamOptimiserService(PlayerRepository playerRepository,
                                 PlayerFeatureService playerFeatureService,
                                 PredictionService predictionService) {
        this.playerRepository = playerRepository;
        this.playerFeatureService = playerFeatureService;
        this.predictionService = predictionService;
    }

    public OptimalSquadDto buildOptimalSquad(int gameweek) {
        Map<Integer, PredictionRequestDto> featuresByPlayerId =
                playerFeatureService.buildFeaturesForAllPlayers(gameweek);

        List<Integer> playerIds = new ArrayList<>(featuresByPlayerId.keySet());
        List<PredictionRequestDto> features = playerIds.stream()
                .map(featuresByPlayerId::get)
                .toList();

        BatchPredictionResponseDto predictionResponse =
                predictionService.getBatchPredictions(new BatchPredictionRequestDto(features));

        Map<Integer, Double> predictedPointsByPlayerId = new HashMap<>();
        for (int i = 0; i < playerIds.size(); i++) {
            predictedPointsByPlayerId.put(playerIds.get(i), predictionResponse.getPredictions().get(i));
        }

        List<Player> candidates = playerRepository.findAllById(playerIds);

        // Ranks candidates within each position by predicted points per
        // unit cost, so the squad favours efficient picks rather than
        // simply the highest-scoring players regardless of price.
        Map<String, List<Player>> byPosition = new HashMap<>();
        for (Player player : candidates) {
            byPosition.computeIfAbsent(player.getPosition(), k -> new ArrayList<>()).add(player);
        }
        for (List<Player> players : byPosition.values()) {
            players.sort((a, b) -> {
                double valueA = predictedPointsByPlayerId.getOrDefault(a.getId(), 0.0) / a.getNowCost();
                double valueB = predictedPointsByPlayerId.getOrDefault(b.getId(), 0.0) / b.getNowCost();
                return Double.compare(valueB, valueA);
            });
        }

        List<SelectedPlayerDto> squad = new ArrayList<>();
        Map<Integer, Integer> clubCounts = new HashMap<>();
        int remainingBudget = BUDGET;

        for (Map.Entry<String, Integer> slot : SQUAD_SHAPE.entrySet()) {
            String position = slot.getKey();
            int slotsNeeded = slot.getValue();
            int filled = 0;

            for (Player candidate : byPosition.getOrDefault(position, List.of())) {
                if (filled == slotsNeeded) break;

                int clubId = candidate.getTeam().getId();
                int cost = candidate.getNowCost();
                int currentClubCount = clubCounts.getOrDefault(clubId, 0);

                if (currentClubCount >= MAX_PER_CLUB) continue;
                if (cost > remainingBudget) continue;

                squad.add(new SelectedPlayerDto(
                        candidate.getId(),
                        candidate.getFirstName() + " " + candidate.getSecondName(),
                        candidate.getPosition(),
                        clubId,
                        cost,
                        predictedPointsByPlayerId.getOrDefault(candidate.getId(), 0.0),
                        candidate.getCode()
                ));

                clubCounts.put(clubId, currentClubCount + 1);
                remainingBudget -= cost;
                filled++;
            }
        }

        int totalCost = squad.stream().mapToInt(SelectedPlayerDto::getCost).sum();
        double totalPoints = squad.stream().mapToDouble(SelectedPlayerDto::getPredictedPoints).sum();

        return new OptimalSquadDto(squad, totalCost, totalPoints);
    }
}