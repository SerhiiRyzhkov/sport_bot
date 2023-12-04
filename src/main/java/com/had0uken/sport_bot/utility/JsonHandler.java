package com.had0uken.sport_bot.utility;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.had0uken.sport_bot.model.League;
import com.had0uken.sport_bot.model.Team;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

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
            String teamId = teamNode.path("Tid").asText();
            String teamName = teamNode.path("Tnm").asText();

            teams.add(new Team(teamId, teamName, league));
        }

        // Now 'teams' list contains the extracted Team objects

        return teams;
    }



}
