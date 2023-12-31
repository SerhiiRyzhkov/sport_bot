package com.had0uken.sport_bot.service.implementation;

import com.had0uken.sport_bot.model.Team;
import com.had0uken.sport_bot.model.User;
import com.had0uken.sport_bot.repository.UserRepository;
import com.had0uken.sport_bot.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.stream.Collectors;
@Slf4j
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;


    @Override
    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public boolean isExist(Long chatId) {
        return   userRepository.existsById(chatId);
    }

    @Override
    public void save(User user)
    {
        log.info("New user was saved: "+user);
        userRepository.save(user);
    }

    @Override
    public String delete(Long chatId) {
        Optional<User> userOptional = userRepository.findById(chatId);
        if(userOptional.isPresent()){
            userRepository.delete(userOptional.get());
            log.info("user" + userOptional.get() + " deleted data");
            return "We deleted all your data";
        }
        else {
            return "We do not keep any of your data";
        }
    }

    @Override
    public String getData(long chatId) {
        Optional<User> userOptional = userRepository.findById(chatId);
        if(userOptional.isPresent()){
            User user = userOptional.get();
            return "Chat ID: "+user.getChatId()+"\n"+
                    "First Name: "+user.getFirstName()+"\n"+
                    "Last Name: "+user.getLastName()+"\n"+
                    "Registration: "+user.getRegisteredAt()+"\n"+
                    "Username: :"+user.getUsername()+"\n+" +
                    "Teams: " + user.getTeams();
        }
        else return "Your data is clear";
    }

    @Override
    public void addTeam(User user, Team team) {
        if(!user.getTeams().contains(team))user.getTeams().add(team);
        userRepository.save(user);
        log.info("user" + user + " saved a team: " + team);
    }

    @Override
    public void deleteTeam(User user, Team team) {
        user.getTeams().remove(team);
        userRepository.save(user);
        log.info("user" + user + " deleted a team: " + team);
    }


}
