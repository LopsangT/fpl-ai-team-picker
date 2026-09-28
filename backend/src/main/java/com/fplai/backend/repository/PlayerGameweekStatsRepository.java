package com.fplai.backend.repository;

import com.fplai.backend.entity.PlayerGameweekStats;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.Query;

public interface PlayerGameweekStatsRepository extends JpaRepository<PlayerGameweekStats, Integer> {

  /*  
  * Checks for existing player-gameweek records to prevent duplicate rows during re-ingestion.
  */
  @Query("SELECT p FROM PlayerGameweekStats p WHERE p.player.id = :playerId AND p.season = :season AND p.gameweek = :gameweek")
  Optional<PlayerGameweekStats> findByPlayerIdAndSeasonAndGameweek(
            Integer playerId, String season, Integer gameweek);
  
  /*
  * Fetches historical gameweek records prior to the target gameweek to compute rolling
  * form and expected-minutes features without data leakage.
  */
  List<PlayerGameweekStats> findByPlayerIdAndSeasonAndGameweekLessThanOrderByGameweekDesc(
        Integer playerId, String season, Integer gameweek);
}