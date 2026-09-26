package com.fplai.backend.dto.fpl;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class FplLiveResponse {
  private List<FplLiveElementDto> elements;

  public List<FplLiveElementDto> getElements() { return elements; }
  public void setElements(List<FplLiveElementDto> elements) { this.elements = elements; }
}


