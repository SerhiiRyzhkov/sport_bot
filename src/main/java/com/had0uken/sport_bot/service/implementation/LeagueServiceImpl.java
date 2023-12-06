package com.had0uken.sport_bot.service.implementation;

import com.had0uken.sport_bot.model.League;
import com.had0uken.sport_bot.repository.LeagueRepository;
import com.had0uken.sport_bot.service.LeagueService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LeagueServiceImpl implements LeagueService {

    @Autowired
    private LeagueRepository leagueRepository;

    @Override
    public List<League> getAllLeagues() {
        return leagueRepository.findAll();
    }
}
