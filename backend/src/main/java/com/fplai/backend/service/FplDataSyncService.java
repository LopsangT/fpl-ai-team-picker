package com.fplai.backend.service;

import com.fplai.backend.dto.fpl.FplFixtureDto;
import com.fplai.backend.dto.fpl.FplPlayerDto;
import com.fplai.backend.dto.fpl.FplTeamDto;
import com.fplai.backend.entity.Fixture;
import com.fplai.backend.entity.Player;
import com.fplai.backend.entity.Team;
import com.fplai.backend.repository.FixtureRepository;
import com.fplai.backend.repository.PlayerGameweekStatsRepository;
import com.fplai.backend.repository.PlayerRepository;
import com.fplai.backend.repository.TeamRepository;
import org.springframework.stereotype.Service;
import com.fplai.backend.dto.fpl.FplLiveElementDto;
import com.fplai.backend.entity.PlayerGameweekStats;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static com.fplai.backend.config.SeasonConfig.CURRENT_SEASON;

@Service
public class FplDataSyncService {

  private final FplApiClient fplApiClient;
  private final TeamRepository teamRepository;
  private final PlayerRepository playerRepository;
  private final FixtureRepository fixtureRepository;
  private final PlayerGameweekStatsRepository playerGameweekStatsRepository;

  public FplDataSyncService(FplApiClient fplApiClient,
                            TeamRepository teamRepository,
                            PlayerRepository playerRepository,
                            FixtureRepository fixtureRepository,
                            PlayerGameweekStatsRepository playerGameweekStatsRepository) {
    this.fplApiClient = fplApiClient;
    this.teamRepository = teamRepository;
    this.playerRepository = playerRepository;
    this.fixtureRepository = fixtureRepository;
    this.playerGameweekStatsRepository = playerGameweekStatsRepository;
  }

  // Live FPL data is fetched and saved into Postgres. Teams are synced
  // first and returned as a lookup map because players and fixtures
  // reference teams by id and need that relationship set before saving.
  public void syncAll() {
    var bootstrapData = fplApiClient.fetchBootstrapData();

    Map<Integer, Team> savedTeams = syncTeams(bootstrapData.getTeams());
    syncPlayers(bootstrapData.getElements(), savedTeams);
    syncFixtures(savedTeams);
  }

  private Map<Integer, Team> syncTeams(List<FplTeamDto> teamDtos) {
    Map<Integer, Team> teamsById = new HashMap<>();

    for (FplTeamDto dto : teamDtos) {
        Team team = new Team();
        team.setId(dto.getId());
        team.setName(dto.getName());
        team.setShortName(dto.getShortName());
        team.setStrengthOverallHome(dto.getStrengthOverallHome());
        team.setStrengthOverallAway(dto.getStrengthOverallAway());

        Team saved = teamRepository.save(team);
        teamsById.put(saved.getId(), saved);
    }

    return teamsById;
  }

  private void syncPlayers(List<FplPlayerDto> playerDtos, Map<Integer, Team> teamsById) {
    for (FplPlayerDto dto : playerDtos) {
      Player player = new Player();
      player.setId(dto.getId());
      player.setFirstName(dto.getFirstName());
      player.setSecondName(dto.getSecondName());
      player.setTeam(teamsById.get(dto.getTeam()));
      player.setPosition(mapPositionCode(dto.getElementType()));
      player.setNowCost(dto.getNowCost());
      player.setStatus(dto.getStatus());
      player.setChanceOfPlayingThisRound(dto.getChanceOfPlayingThisRound());
      player.setNews(dto.getNews());

      playerRepository.save(player);
    }
  }

  private void syncFixtures(Map<Integer, Team> teamsById) {
    var fixtureDtos = fplApiClient.fetchFixtures();

    for (FplFixtureDto dto : fixtureDtos) {
      if (dto.getGameweek() == null) continue;   

        Fixture fixture = fixtureRepository
          .findBySeasonAndHomeTeamIdAndAwayTeamIdAndGameweek(
                  CURRENT_SEASON, dto.getTeamHome(), dto.getTeamAway(), dto.getGameweek())
          .orElse(new Fixture());   // reuse existing row if present, otherwise create new

        fixture.setSeason(CURRENT_SEASON);
        fixture.setGameweek(dto.getGameweek());
        fixture.setHomeTeam(teamsById.get(dto.getTeamHome()));
        fixture.setAwayTeam(teamsById.get(dto.getTeamAway()));
        fixture.setHomeDifficulty(dto.getTeamHomeDifficulty());
        fixture.setAwayDifficulty(dto.getTeamAwayDifficulty());
        fixture.setFinished(dto.getFinished() != null && dto.getFinished());

        if (dto.getKickoffTime() != null) {
            fixture.setKickoffTime(
                    LocalDateTime.parse(dto.getKickoffTime(), DateTimeFormatter.ISO_DATE_TIME));
        }

        fixtureRepository.save(fixture);
    }
  }

  // Converts FPL's numeric element_type into the schema position code
  private String mapPositionCode(Integer elementType) {
    return switch (elementType) {
      case 1 -> "GKP";
      case 2 -> "DEF";
      case 3 -> "MID";
      case 4 -> "FWD";
      default -> throw new IllegalArgumentException("Unknown element_type: " + elementType);
    };
  }

  public void syncGameweekStats(int gameweek) {
    var liveData = fplApiClient.fetchGameweekLive(gameweek);
    List<Fixture> gameweekFixtures = fixtureRepository.findBySeasonAndGameweek(CURRENT_SEASON, gameweek);

    for (FplLiveElementDto element : liveData.getElements()) {
      // Filter out players with 0 minutes who lack matchday fixture metrics
      if (element.getStats().getMinutes() == 0) continue;

      Player player = playerRepository.findById(element.getId()).orElse(null);
      if (player == null) continue;

      Integer playerTeamId = player.getTeam().getId();

      Fixture fixture = gameweekFixtures.stream()
        .filter(f -> f.getHomeTeam().getId().equals(playerTeamId)
                  || f.getAwayTeam().getId().equals(playerTeamId))
        .findFirst()
        .orElse(null);
      if (fixture == null) continue;

      boolean wasHome = fixture.getHomeTeam().getId().equals(playerTeamId);
      Integer opponentTeamId = wasHome ? fixture.getAwayTeam().getId() : fixture.getHomeTeam().getId();

      PlayerGameweekStats stats = playerGameweekStatsRepository
        .findByPlayerIdAndSeasonAndGameweek(player.getId(), CURRENT_SEASON, gameweek)
        .orElse(new PlayerGameweekStats());

      stats.setPlayer(player);
      stats.setSeason(CURRENT_SEASON);
      stats.setGameweek(gameweek);
      stats.setFixture(fixture);
      stats.setOpponentTeam(teamRepository.findById(opponentTeamId).orElse(null));
      stats.setWasHome(wasHome);
      stats.setMinutesPlayed(element.getStats().getMinutes());
      stats.setGoalsScored(element.getStats().getGoalsScored());
      stats.setAssists(element.getStats().getAssists());
      stats.setCleanSheets(element.getStats().getCleanSheets());
      stats.setGoalsConceded(element.getStats().getGoalsConceded());
      stats.setSaves(element.getStats().getSaves());
      stats.setBonus(element.getStats().getBonus());
      stats.setTotalPoints(element.getStats().getTotalPoints());
      // form_before_gameweek is calculated at training time from
      // historical rows and is intentionally left null during ingestion.

      playerGameweekStatsRepository.save(stats);
    }
  }
}
