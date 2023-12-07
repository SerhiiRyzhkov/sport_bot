package com.had0uken.sport_bot.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "leagues")
public class League implements Serializable {

    @Serial
    private static final long serialVersionUID = 5787545020193329483L;

    @JsonProperty("Sid")
    @Column(name = "LEAGUE_ID")
    @Id
    private Long league_id;

    @JsonProperty("Cnm")
    @Column(name = "LEAGUE_NAME")
    private String league_name;


    @JsonProperty("Scd")
    @Column(name = "LEAGUE_CODE")
    private String league_code;

    @JsonProperty("Cid")
    @Column(name = "COUNTRY_ID")
    private Long country_id;

    @JsonProperty("Ccd")
    @Column(name = "COUNTRY_CODE")
    private String country_code;

    @JsonProperty("CompId")
    @Column(name = "COMPETITION_ID")
    private Long competition_id;

    @JsonProperty("CompN")
    @Column(name = "COMPETITION_NAME")
    private String competition_name;


    @JsonProperty("Sdn")
    @Column(name = "STAGE_NAME")
    private String stage_name;

    @OneToMany(mappedBy = "league", fetch = FetchType.EAGER)
    private List<Team> teams;

    @Override
    public String toString() {
        return "League{" +
                "league_id=" + league_id +
                ", league_name='" + league_name + '\'' +
                '}';
    }
}