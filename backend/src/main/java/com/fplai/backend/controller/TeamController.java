package com.fplai.backend.controller;

import com.fplai.backend.dto.team.OptimalSquadDto;
import com.fplai.backend.service.TeamOptimiserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/team")
public class TeamController {

    private final TeamOptimiserService teamOptimiserService;

    public TeamController(TeamOptimiserService teamOptimiserService) {
        this.teamOptimiserService = teamOptimiserService;
    }

    @GetMapping("/optimal/gameweek/{gameweek}") 
    public OptimalSquadDto getOptimalSquad(@PathVariable int gameweek) {
        return teamOptimiserService.buildOptimalSquad(gameweek);
    }
}
