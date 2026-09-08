package com.fplai.backend.controller;

import com.fplai.backend.service.FplDataSyncService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController 
@RequestMapping("/api/sync")
public class SyncController {
  private final FplDataSyncService syncService;

  public SyncController(FplDataSyncService syncService) {
    this.syncService = syncService;
  }

  // Manually trigger a sync of teams/players from the live FPL API
  @PostMapping("fpl-data")
  public ResponseEntity<String> syncFplData() {
    syncService.syncTeamsAndPlayers();
    return ResponseEntity.ok("Sync completed successfully");
  }
}
