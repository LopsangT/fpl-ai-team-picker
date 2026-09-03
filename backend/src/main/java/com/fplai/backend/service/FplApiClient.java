package com.fplai.backend.service;

import com.fplai.backend.dto.fpl.FplBootstrapResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class FplApiClient {

  private final RestClient fplRestClient;

  public FplApiClient(RestClient fplRestClient) {
    this.fplRestClient = fplRestClient;
  }

  public FplBootstrapResponse fetchBootstrapData() {
    return fplRestClient.get()
      .uri("/bootstrap-static/")
      .retrieve()
      .body(FplBootstrapResponse.class);
  }
}
