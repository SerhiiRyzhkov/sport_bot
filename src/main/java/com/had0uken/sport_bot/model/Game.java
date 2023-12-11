package com.had0uken.sport_bot.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Game {
    private String Sid;
    private String Eid;
    private Team team1;
    private Team team2;
    private Integer Tr1OR;
    private Integer Tr2OR;

    @Override
    public String toString() {
        return team1.getTEAM_NAME()+" "+ Tr1OR + ":"+Tr2OR + " " + team2.getTEAM_NAME();
    }
}