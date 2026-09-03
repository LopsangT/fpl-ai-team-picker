package com.fplai.backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfig {
  
  // Using a single shared RestClient for calling the FPL API - by setting
  // base URL here, calling code only needs to specify the path, not the full
  // domain every time
  @Bean 
  public RestClient fplRestClient() {
    return RestClient.builder()
      .baseUrl("https://fantasy.premierleague.com/api")
      .build();
  }
}