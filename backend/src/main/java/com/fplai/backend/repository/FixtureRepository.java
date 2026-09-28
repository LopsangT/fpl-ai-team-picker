package com.fplai.backend.repository;

import com.fplai.backend.entity.Fixture;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.List;

public interface FixtureRepository extends JpaRepository<Fixture, Integer> {

  // Spring Data JPA generates the query from this method name -
  // used to check if a fixture already exists before inserting, so
  // repeated syncs update rather than duplicate
  Optional<Fixture> findBySeasonAndHomeTeamIdAndAwayTeamIdAndGameweek(
    String season, Integer homeTeamId, Integer awayTeamId, Integer gameweek);
  List<Fixture> findBySeasonAndGameweek(String season, Integer gameweek);
}