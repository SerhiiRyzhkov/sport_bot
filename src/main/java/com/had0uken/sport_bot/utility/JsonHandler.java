package com.had0uken.sport_bot.utility;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.had0uken.sport_bot.model.Game;
import com.had0uken.sport_bot.model.League;
import com.had0uken.sport_bot.model.Team;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.*;

@Component
public class JsonHandler {

    public static List<Team> parseTeamsFromJson(String jsonString, League league) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = objectMapper.readTree(jsonString);
        List<Team> teams = new ArrayList<>();
        JsonNode teamsNode = rootNode.path("LeagueTable").path("L").get(0).path("Tables").get(0).path("team");
        Iterator<JsonNode> teamIterator = teamsNode.elements();

        while (teamIterator.hasNext()) {
            JsonNode teamNode = teamIterator.next();
            Long teamId = Long.valueOf(teamNode.path("Tid").asText());
            String teamName = teamNode.path("Tnm").asText();
            teams.add(new Team(teamId, teamName, league,new HashSet<>()));
        }
        return teams;
    }

    public static List<Game> parseResultsFromJson(String jsonData) throws IOException{
        List<Game> games = new ArrayList<>();
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, List<Map<String, Object>>> jsonMap = objectMapper.readValue(jsonData, Map.class);
            games = new ArrayList<>();
            List<Map<String, Object>> stages = jsonMap.get("Stages");

            for (Map<String, Object> stage : stages) {
                List<Map<String, Object>> events = (List<Map<String, Object>>) stage.get("Events");
                for (Map<String, Object> event : events) {
                    Game game = new Game();
                    if(event.containsKey("Tr1OR")&&(event.containsKey("Tr2OR"))) {
                        game.setSid((String) stage.get("Sid"));
                        game.setEid((String) event.get("Eid"));
                        game.setTeam1(createTeam((List<Map<String, Object>>) event.get("T1")));
                        game.setTeam2(createTeam((List<Map<String, Object>>) event.get("T2")));
                        game.setTr1OR((Integer.valueOf(event.get("Tr1OR").toString())));
                        game.setTr2OR((Integer.valueOf(event.get("Tr2OR").toString())));
                        games.add(game);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return games;
    }



        private static Team createTeam(List<Map<String, Object>> teamData) {
            Team team = new Team();
            team.setTEAM_NAME(teamData.get(0).get("Nm").toString());
            team.setTEAM_ID(Long.valueOf(teamData.get(0).get("ID").toString()) );
            return team;
        }


    }




