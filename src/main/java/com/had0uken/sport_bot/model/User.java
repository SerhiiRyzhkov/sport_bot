package com.had0uken.sport_bot.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

import java.io.Serial;
import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "usersDataTable")
public class User implements Serializable {

    @Serial
    private static final long serialVersionUID = -3423040518283131848L;

    @Id
    private Long chatId;

    private String firstName;

    private String lastName;

    private String username;

    private Timestamp registeredAt;

}