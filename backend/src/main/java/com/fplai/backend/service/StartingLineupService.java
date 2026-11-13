package com.fplai.backend.service;

import com.fplai.backend.dto.team.OptimalSquadDto;
import com.fplai.backend.dto.team.SelectedPlayerDto;
import com.fplai.backend.dto.team.StartingLineupDto;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Creates the starting XI and a 4 player bench while following FPL's formation rules: exactly 1 GKP, miinimum of 3 DEF, 2 MID and at least
 * 1 FWD totalling 11 players on the pitch.
 * 
 * Minimum position requirements are filled first with the players that are predicted to score the highest in each position, then the remaining
 * flexible slots are filled from whichever outfield players are predicted to score the highest between the one that are left over.
 */
@Service 
public class StartingLineupService {

    private static final int MIN_DEF = 3;
    private static final int MIN_MID = 2;
    private static final int MIN_FWD = 1;
    private static final int STARTING_OUTFIELD_COUNT = 10;

    public StartingLineupDto pickStartingLineup(OptimalSquadDto squad) {
        List<SelectedPlayerDto> players = squad.getPlayers();

        List<SelectedPlayerDto> goalkeepers = filterByPosition(players, "GKP");
        List<SelectedPlayerDto> defenders = filterByPosition(players, "DEF");
        List<SelectedPlayerDto> midfielders = filterByPosition(players, "MID");
        List<SelectedPlayerDto> forwards = filterByPosition(players, "FWD");

        List<SelectedPlayerDto> sortedGoalkeepers = sortByPredictedPointsDescending(goalkeepers);
        List<SelectedPlayerDto> sortedDefenders = sortByPredictedPointsDescending(defenders);
        List<SelectedPlayerDto> sortedMidfielders = sortByPredictedPointsDescending(midfielders);
        List<SelectedPlayerDto> sortedForwards = sortByPredictedPointsDescending(forwards);

        // Minimum-satisfying starters: the top MIN_DEF defenders, top MIN_MID midfielders, top MIN_FWD forward - guaranteed to start.
        List<SelectedPlayerDto> startingGoalkeeper = sortedGoalkeepers.subList(0, 1);
        List<SelectedPlayerDto> guaranteedDefenders = sortedDefenders.subList(0, MIN_DEF);
        List<SelectedPlayerDto> guaranteedMidfielders = sortedMidfielders.subList(0, MIN_MID);
        List<SelectedPlayerDto> guaranteedForwards = sortedForwards.subList(0, MIN_FWD);

        int guaranteedCount = guaranteedDefenders.size() + guaranteedMidfielders.size() + guaranteedForwards.size();
        int flexibleSlotsRemaining = STARTING_OUTFIELD_COUNT - guaranteedCount;

        // Everyone NOT yet guaranteed a starting spot, pooled together
        // from all three outfield positions.
        List<SelectedPlayerDto> remainingCandidates = new ArrayList<>();
        remainingCandidates.addAll(sortedDefenders.subList(MIN_DEF, sortedDefenders.size()));
        remainingCandidates.addAll(sortedMidfielders.subList(MIN_MID, sortedMidfielders.size()));
        remainingCandidates.addAll(sortedForwards.subList(MIN_FWD, sortedForwards.size()));

        // flexibleStarters
        List<SelectedPlayerDto> sortedRemaining = sortByPredictedPointsDescending(remainingCandidates);
        List<SelectedPlayerDto> flexibleStarters = sortedRemaining.subList(0, flexibleSlotsRemaining);

        List<SelectedPlayerDto> startingXi = new ArrayList<>();
        startingXi.addAll(startingGoalkeeper);
        startingXi.addAll(guaranteedDefenders);
        startingXi.addAll(guaranteedMidfielders);
        startingXi.addAll(guaranteedForwards);
        startingXi.addAll(flexibleStarters);

        // bench
        Set<Integer> startingXiIds = startingXi.stream()
            .map(SelectedPlayerDto::getPlayerId)
            .collect(Collectors.toSet());

        List<SelectedPlayerDto> bench = players.stream()
            .filter(p -> !startingXiIds.contains(p.getPlayerId()))
            .collect(Collectors.toList());

        String formation = guaranteedDefenders.size() + flexibleDefenderCount(flexibleStarters)
                + "-" + (guaranteedMidfielders.size() + flexibleMidfielderCount(flexibleStarters))
                + "-" + (guaranteedForwards.size() + flexibleForwardCount(flexibleStarters));

        return new StartingLineupDto(startingXi, bench, formation);
    }

    private List<SelectedPlayerDto> filterByPosition(List<SelectedPlayerDto> players, String position) {
        return players.stream()
                .filter(p -> p.getPosition().equals(position))
                .collect(Collectors.toList());
    }

    private List<SelectedPlayerDto> sortByPredictedPointsDescending(List<SelectedPlayerDto> players) {
        List<SelectedPlayerDto> sorted = new ArrayList<>(players);
        sorted.sort((a, b) -> Double.compare(b.getPredictedPoints(), a.getPredictedPoints()));
        return sorted;
    }

    private long flexibleDefenderCount(List<SelectedPlayerDto> flexibleStarters) {
        return flexibleStarters.stream().filter(p -> p.getPosition().equals("DEF")).count();
    }

    private long flexibleMidfielderCount(List<SelectedPlayerDto> flexibleStarters) {
        return flexibleStarters.stream().filter(p -> p.getPosition().equals("MID")).count();
    }

    private long flexibleForwardCount(List<SelectedPlayerDto> flexibleStarters) {
        return flexibleStarters.stream().filter(p -> p.getPosition().equals("FWD")).count();
    }
}

