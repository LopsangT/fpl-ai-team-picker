package com.fplai.backend.controller;

import com.fplai.backend.service.FplDataSyncService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/api/sync")
public class SyncController {

  private final FplDataSyncService syncService;

  public SyncController(FplDataSyncService syncService) {
    this.syncService = syncService;
  }

  /* Manually trigger a sync of teams, players, and fixtures from the live FPL API */
  @PostMapping("/fpl-data")
    public ResponseEntity<String> syncFplData() {
      syncService.syncAll();
      return ResponseEntity.ok("Sync completed successfully");
  }
  
  /* 
    Manually triggers a sync of single gameweek's player stats from the live FPL API 
    mapping each player to their fixture to determine opponent and venue (home/away) status.
  */
  @PostMapping("/gameweek/{gameweek}")
  public ResponseEntity<String> syncGameweek(@PathVariable int gameweek) {
    syncService.syncGameweekStats(gameweek);
    return ResponseEntity.ok("Gameweek " + gameweek + " sync completed successfully");
  }
}