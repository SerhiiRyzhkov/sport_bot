package com.had0uken.sport_bot.service;

import com.had0uken.sport_bot.model.League;

import java.util.List;
import java.util.Optional;

public interface LeagueService {
    List<League> getAllLeagues();
    Optional<League> getLeagueById(Long id);
}
