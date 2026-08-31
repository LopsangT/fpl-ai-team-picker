package com.fplai.backend.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(
    name = "player_predictions",
    uniqueConstraints = @UniqueConstraint(columnNames = {"player_id", "season", "gameweek"})
)
@Data
@NoArgsConstructor
public class PlayerPrediction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "player_id", nullable = false)
    private Player player;

    @Column(nullable = false, length = 9)
    private String season;

    @Column(nullable = false)
    private Integer gameweek;

    @Column(name = "predicted_points", nullable = false, precision = 5, scale = 2)
    private BigDecimal predictedPoints;

    @Column(name = "predicted_at", nullable = false)
    private LocalDateTime predictedAt = LocalDateTime.now();   // matches DEFAULT NOW() in schema
}