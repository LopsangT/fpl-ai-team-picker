package com.fplai.backend;

import com.fplai.backend.service.FplApiClient;
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
  CommandLineRunner testFplFetch(FplApiClient fplApiClient) {
    return args -> {
      var data = fplApiClient.fetchBootstrapData();
      System.out.println("Fetched " + data.getTeams().size() + " teams");
      System.out.println("Fetched " + data.getElements().size() + " players");
      System.out.println("First team: " + data.getTeams().get(0).getName());
    };
	}
}
