package com.fplai.backend.controller;

import com.fplai.backend.dto.team.OptimalSquadDto;
import com.fplai.backend.dto.team.StartingLineupDto;
import com.fplai.backend.service.StartingLineupService;
import com.fplai.backend.service.TeamOptimiserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/team")
public class TeamController {

    private final TeamOptimiserService teamOptimiserService;
    private final StartingLineupService startingLineupService;

    public TeamController(TeamOptimiserService teamOptimiserService, StartingLineupService startingLineupService) {
        this.teamOptimiserService = teamOptimiserService;
        this.startingLineupService = startingLineupService;
    }

    @GetMapping("/optimal/gameweek/{gameweek}") 
    public OptimalSquadDto getOptimalSquad(@PathVariable int gameweek) {
        return teamOptimiserService.buildOptimalSquad(gameweek);
    }

    @GetMapping("/lineup/gameweek/{gameweek}")
    public StartingLineupDto getStartingLineup(@PathVariable int gameweek) {
        OptimalSquadDto squad = teamOptimiserService.buildOptimalSquad(gameweek);
        return startingLineupService.pickStartingLineup(squad);
    }
}
