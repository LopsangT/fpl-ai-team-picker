package com.fplai.backend.repository;

import com.fplai.backend.entity.PlayerGameweekStats;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface PlayerGameweekStatsRepository extends JpaRepository<PlayerGameweekStats, Integer> {
  Optional<PlayerGameweekStats> findByPlayerIdAndSeasonAndGameweek(
    Integer playerId, String season, Integer gameweek);
}