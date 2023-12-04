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
    @Column(name = "league_id")
    @Id
    private Long league_id;

    @JsonProperty("Cnm")
    @Column(name = "league_name")
    private String league_name;


    @JsonProperty("Scd")
    @Column(name = "league_code")
    private String league_code;

    @JsonProperty("Cid")
    @Column(name = "country_id")
    private Long country_id;

    @JsonProperty("Ccd")
    @Column(name = "country_code")
    private String country_code;

    @JsonProperty("CompId")
    @Column(name = "competition_id")
    private Long competition_id;

    @JsonProperty("CompN")
    @Column(name = "competition_name")
    private String competition_name;


    @JsonProperty("Sdn")
    @Column(name = "stage_name")
    private String stage_name;
}