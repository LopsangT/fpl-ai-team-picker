package com.fplai.backend.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@Entity
@Table(
  name = "player_gameweek_stats",
  uniqueConstraints = @UniqueConstraint(columnNames = {"player_id", "season", "gameweek"})
)
@Data
@NoArgsConstructor
public class PlayerGameweekStats {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "player_id", nullable = false)
  private Player player;

  @Column(nullable = false)
  private Integer gameweek;

  @Column(nullable = false, length = 9)
  private String season;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "fixture_id")
  private Fixture fixture;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "opponent_team_id")
  private Team opponentTeam;

  @Column(name = "was_home", nullable = false)
  private Boolean wasHome;

  @Column(name = "minutes_played", nullable = false)
  private Integer minutesPlayed;

  // Stats stored for reference and for deriving other features but
  // for predicting totalPoints for this same gameweek, they are components of that
  // outcome, so using them directly would leak the answer into training
  @Column(name = "goals_scored", nullable = false)
  private Integer goalScored = 0;

  @Column(nullable = false)
  private Integer assists = 0;

  @Column(name = "goals_conceded", nullable = false)
  private Integer goalsConceded = 0;

  @Column(name = "clean_sheets", nullable = false)
  private Integer cleanSheets = 0;

  @Column(nullable = false)
  private Integer saves = 0;

  @Column(nullable = false)
  private Integer bonus = 0;

  @Column(name = "form_before_gameweek", precision = 4, scale = 2)
  private BigDecimal formBeforeGameweek;

  @Column(name = "total_points", nullable = false)
  private Integer totalPoints;
}

