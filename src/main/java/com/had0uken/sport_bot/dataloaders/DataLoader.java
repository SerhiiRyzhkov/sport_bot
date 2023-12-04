package com.had0uken.sport_bot.dataloaders;

import com.had0uken.sport_bot.model.League;
import com.had0uken.sport_bot.model.Team;
import com.had0uken.sport_bot.repository.TeamRepository;
import com.had0uken.sport_bot.utility.JsonGetter;
import com.had0uken.sport_bot.utility.JsonHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import com.had0uken.sport_bot.repository.LeagueRepository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner
{
    private final LeagueRepository leagueRepository;
    private final TeamRepository teamRepository;
    @Override
    public void run(String... args) throws IOException {
        List<League>leagues = new ArrayList<>();
        //                              Sid                 Cnm                 Scd                     Cid             Ccd                   CompId                CompN                       Sdn
        // ENGLAND, SPAIN, ITALY, NETHERLANDS, GERMANY, PORTUGAL, UKRAINE, FRANCE
        leagues.add(new League(14414L,"England","premier-league",34L,"england",65L,"Premier League", "Premier League",new ArrayList<>()));
        leagues.add(new League(14500L,"Spain","laliga",195L,"spain",75L,"LaLiga", "LaLiga",new ArrayList<>()));
        leagues.add(new League(14689L,"Italy","serie-a",50L,"italy",77L,"Serie A", "Serie A",new ArrayList<>()));
        leagues.add(new League(14579L,"Netherlands","eredivisie",178L,"holland",64L,"Eredivisie", "Eredivisie",new ArrayList<>()));
        leagues.add(new League(14596L,"Germany","bundesliga",137L,"germany",67L,"Bundesliga", "Bundesliga",new ArrayList<>()));
        leagues.add(new League(14706L,"Portugal","primeira-liga",171L,"portugal",79L,"Primeira Liga", "Primeira Liga",new ArrayList<>()));
        leagues.add(new League(2283L,"Ukraine","premier-league-championship-round",112L,"ukraine",164L,"Premier League", "Premier League: Championship Round",new ArrayList<>()));
        leagues.add(new League(14589L,"France","ligue-1",177L,"france",68L,"Ligue 1", "Ligue 1",new ArrayList<>()));
        leagueRepository.saveAll(leagues);


        for(League l: leagues){
            String json = JsonGetter.getLocalJson(l.getCountry_code());
            List<Team>teams = JsonHandler.parseTeamsFromJson(json,l);
            teamRepository.saveAll(teams);
        }
    }


}