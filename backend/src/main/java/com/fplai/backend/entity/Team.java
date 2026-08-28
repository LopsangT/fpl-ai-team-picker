package com.fplai.backend.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "teams")
@Data
@NoArgsConstructor

public class Team {

  @Id
  private Integer id;

  @Column(nullable = false, length = 50)
  private String name;

  @Column(name = "short_name", nullable = false, length = 3)
  private String shortName;

  @Column(name = "strength_overall_home")
  private Integer strengthOverallHome;

  @Column(name = "strength_overall_away")
  private Integer sttrengthOverallAway;
}

