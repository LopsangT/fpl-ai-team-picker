package com.fplai.backend;

import com.fplai.backend.repository.PlayerRepository;
import com.fplai.backend.repository.TeamRepository;
import com.fplai.backend.service.FplApiClient;
import com.fplai.backend.service.FplDataSyncService;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class BackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(BackendApplication.class, args);
	}

  // just for testing, once FplApiClient works this will be removed
	@Bean
  CommandLineRunner testSync(FplDataSyncService syncService,
                              TeamRepository teamRepository,
                              PlayerRepository playerRepository) {
    return args -> {
      syncService.syncTeamsAndPlayers();
      System.out.println("Teams in DB: " + teamRepository.count());
      System.out.println("Players in DB: " + playerRepository.count());
    };
  }
}
