package com.had0uken.sport_bot.repository;

import com.had0uken.sport_bot.model.League;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LeagueRepository extends JpaRepository<League,Long> {
}