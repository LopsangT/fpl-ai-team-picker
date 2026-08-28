package com.fplai.backend.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "players")
@Data
@NoArgsConstructor

public class Player {

  @Id
  private Integer id; 

  @ManyToOne(fetch = FetchType.LAZY)

  @JoinColumn(name = "team_id", nullable = false)
  private Team team;

  @Column(name = "first_name", nullable = false, length = 50)
  private String firstName;

  @Column(name = "second_name", nullable = false, length = 50)
  private String secondName;

  @Column(nullable = false, length = 3)
  private String position;

  @Column(name = "now_cost", nullable = false)
  private Integer nowCost;

  @Column(nullable = false, length = 1)
  private String status;

  @Column(name = "chance_of_playing_this_round")
  private Integer chanceOfPlayingThisRound;

  @Column(columnDefinition = "TEXT")
  private String news;
}