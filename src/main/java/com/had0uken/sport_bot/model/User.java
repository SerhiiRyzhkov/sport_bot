package com.had0uken.sport_bot.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "usersDataTable")
public class User implements Serializable {

    @Serial
    private static final long serialVersionUID = -3423040518283131848L;

    @Id
    @Column(name = "CHAT_ID")
    private Long chatId;
    @Column(name = "FIRST_NAME")
    private String firstName;
    @Column(name = "LAST_NAME")
    private String lastName;
    @Column(name = "USERNAME")
    private String username;
    @Column(name = "TIMESTAMP")
    private Timestamp registeredAt;

    @ManyToMany(cascade = { CascadeType.ALL }, fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_team",
            joinColumns = { @JoinColumn(name = "user_chat_id") },
            inverseJoinColumns = { @JoinColumn(name = "team_id") }
    )
    private List<Team> teams = new ArrayList<>();

    @Override
    public String toString() {
        return "User{" +
                "chatId=" + chatId +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", username='" + username + '\'' +
                '}';
    }
}