package com.fplai.backend.repository;

import com.fplai.backend.entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;


public interface TeamRepository extends JpaRepository<Team, Integer> {

  
}