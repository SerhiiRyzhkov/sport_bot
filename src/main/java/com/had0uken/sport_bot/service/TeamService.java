package com.had0uken.sport_bot.service;

import com.had0uken.sport_bot.model.Team;

import java.util.List;
import java.util.Optional;

public interface TeamService {
    List<Team> getAllTeams();
    Optional<Team> getTeamById(Long id);

}
