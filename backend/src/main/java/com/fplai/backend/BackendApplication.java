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
  
}
