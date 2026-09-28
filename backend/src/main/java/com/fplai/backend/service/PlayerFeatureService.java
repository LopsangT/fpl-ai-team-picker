package com.fplai.backend.service;

import com.fplai.backend.dto.ml.PredictionRequestDto;
import com.fplai.backend.entity.Fixture;
import com.fplai.backend.entity.Player;
import com.fplai.backend.entity.PlayerGameweekStats;
import com.fplai.backend.repository.FixtureRepository;
import com.fplai.backend.repository.PlayerGameweekStatsRepository;
import com.fplai.backend.repository.PlayerRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import com.fplai.backend.entity.Team;
import com.fplai.backend.repository.TeamRepository;
import static com.fplai.backend.config.SeasonConfig.CURRENT_SEASON;

@Service
public class PlayerFeatureService {

    private final TeamRepository teamRepository;
    private static final int FORM_WINDOW = 3;

    private final PlayerRepository playerRepository;
    private final PlayerGameweekStatsRepository statsRepository;
    private final FixtureRepository fixtureRepository;

    public PlayerFeatureService(PlayerRepository playerRepository,
                                PlayerGameweekStatsRepository statsRepository,
                                FixtureRepository fixtureRepository,
                                TeamRepository TeamRepository, TeamRepository teamRepository) {
        
        this.playerRepository = playerRepository;
        this.statsRepository = statsRepository;
        this.fixtureRepository = fixtureRepository;
        this.teamRepository = teamRepository;
    }

    public PredictionRequestDto buildFeatures(Integer playerId, int gameweek) {
        Player player = playerRepository.findById(playerId)
            .orElseThrow(() -> new IllegalArgumentException("Player not found: " + playerId));

        List<PlayerGameweekStats> recentGames = statsRepository
            .findByPlayerIdAndSeasonAndGameweekLessThanOrderByGameweekDesc(
                playerId, CURRENT_SEASON, gameweek)
                .stream()
                .limit(FORM_WINDOW)
                .toList();
        
        double formBeforeGameweek = recentGames.stream()
            .mapToInt(PlayerGameweekStats::getTotalPoints)
            .average()
            .orElse(0.0);
        
        // Uses recent average playing time as a proxy for unknown pre-match kickoff minutes.
        int expectedMinutes = (int) recentGames.stream()
                .mapToInt(PlayerGameweekStats::getMinutesPlayed)
                .average()
                .orElse(0.0);
        
        Integer teamId = player.getTeam().getId();
        Fixture fixture = fixtureRepository.findBySeasonAndGameweek(CURRENT_SEASON, gameweek)
                .stream()
                .filter(f -> f.getHomeTeam().getId().equals(teamId)
                        ||  f.getAwayTeam().getId().equals(teamId))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("No fixture found for team " + teamId + " in gameweek " + gameweek));
        
        boolean wasHome = fixture.getHomeTeam().getId().equals(teamId);
        int opponentTeamId = wasHome
                ? fixture.getAwayTeam().getId()
                : fixture.getHomeTeam().getId();
        
        Team opponent = teamRepository.findById(opponentTeamId)
            .orElseThrow(() -> new IllegalStateException("Team not found: " + opponentTeamId));
        
        int opponentDifficulty = wasHome
        ? opponent.getStrengthOverallAway()
        : opponent.getStrengthOverallHome();

        return new PredictionRequestDto(
            wasHome, expectedMinutes, formBeforeGameweek, opponentDifficulty, player.getPosition());
    }
}
