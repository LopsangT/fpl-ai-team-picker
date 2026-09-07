package com.fplai.backend.service;

import com.fplai.backend.dto.fpl.FplPlayerDto;
import com.fplai.backend.dto.fpl.FplTeamDto;
import com.fplai.backend.entity.Player;
import com.fplai.backend.entity.Team;
import com.fplai.backend.repository.PlayerRepository;
import com.fplai.backend.repository.TeamRepository;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class FplDataSyncService {
  
  private final FplApiClient fplApiClient;
  private final TeamRepository teamRepository;
  private final PlayerRepository playerRepository;

  public FplDataSyncService(FplApiClient fplApiClient,
                            TeamRepository teamRepository,
                            PlayerRepository playerRepository) {
      this.fplApiClient = fplApiClient;
      this.teamRepository = teamRepository;
      this.playerRepository = playerRepository;
  }

  // Live FPL data is fetched and saved into Postgre. Teams are synced first
  // and returned as a lookup map because players reference their team by id
  // and need that relationship set before saving
  public void syncTeamsAndPlayers() {
    var bootstrapData = fplApiClient.fetchBootstrapData();

    Map<Integer, Team> savedTeams = syncTeams(bootstrapData.getTeams());
    syncPlayers(bootstrapData.getElements(), savedTeams);
  }

  private Map<Integer, Team> syncTeams(java.util.List<FplTeamDto> teamDtos) {
    Map<Integer, Team> teamsById = new HashMap<>();

    for (FplTeamDto dto: teamDtos) {
      Team team = new Team();
      team.setId(dto.getId());
      team.setName(dto.getName());
      team.setShortName(dto.getShortName());
      team.setStrengthOverallHome(dto.getStrengthOverallHome());
      team.setSttrengthOverallAway(dto.getStrengthOverallAway());

      Team saved = teamRepository.save(team);
      teamsById.put(saved.getId(), saved);
    }

    return teamsById;
  }

  private void syncPlayers(java.util.List<FplPlayerDto> playerDtos, Map<Integer, Team> teamsById) {
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

  // For schema position codes, the FPL's numeric element_type will be converted 
  private String mapPositionCode(Integer elementType) {
    return switch (elementType) {
      case 1 -> "GKP";
      case 2 -> "DEF";
      case 3 -> "MID";
      case 4 -> "FWD";
      default -> throw new IllegalArgumentException("Unknown element_type: " + elementType);
    };
  }
}