package com.fplai.backend.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "fixtures")
@Data
@NoArgsConstructor
public class Fixture {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Column(nullable = false, length = 9)
  private String season;

  @Column(nullable = false)
  private Integer gameweek;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "home_team_id", nullable = false)
  private Team homeTeam;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "away_team_id", nullable = false)
  private Team awayTeam;

  @Column(name = "home_difficulty")
  private Integer homeDifficulty;

  @Column(name = "away_difficulty")
  private Integer awayDifficulty;

  @Column(name = "kickoff_time")
  private LocalDateTime kickoffTime;

  @Column(nullable = false)
  private Boolean finished = false;
}