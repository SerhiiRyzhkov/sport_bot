package com.had0uken.sport_bot.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "teams")
public class Team implements Serializable {
    @Serial
    private static final long serialVersionUID = -4824171355003629367L;

    @JsonProperty("Tid")
    @Column(name = "TEAM_ID")
    @Id
    private Long TEAM_ID;

    @JsonProperty("Tnm")
    @Column(name = "TEAM_NAME")
    private String TEAM_NAME;


    @ManyToOne
    @JoinColumn(name = "LEAGUE_ID", referencedColumnName = "LEAGUE_ID")
    private League league;

    @ManyToMany(mappedBy = "teams", fetch = FetchType.EAGER)
    private Set<User> users = new HashSet<>();
    @Override
    public String toString() {
        return "Team{" +
                "TEAM_NAME='" + TEAM_NAME + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Team team = (Team) o;
        return Objects.equals(TEAM_ID, team.TEAM_ID);
    }

    @Override
    public int hashCode() {
        return Objects.hash(TEAM_ID);
    }
}