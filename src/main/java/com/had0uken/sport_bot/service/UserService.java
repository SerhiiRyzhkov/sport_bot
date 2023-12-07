package com.had0uken.sport_bot.service;

import com.had0uken.sport_bot.model.Team;
import com.had0uken.sport_bot.model.User;

import java.util.Optional;

public interface UserService {
    Optional<User> getUserById(Long id);
    boolean isExist(Long chatId);
    void save(User user);

    String delete(Long chatId);

    String getData(long chatId);

    void addTeam(User user, Team team);
}
