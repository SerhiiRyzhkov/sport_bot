package com.had0uken.sport_bot.service.implementation;

import com.had0uken.sport_bot.model.User;
import com.had0uken.sport_bot.repository.UserRepository;
import com.had0uken.sport_bot.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;


    @Override
    public boolean isExist(Long chatId) {
        return   userRepository.existsById(chatId);
    }

    @Override
    public void save(User user) {
        userRepository.save(user);
    }

    @Override
    public void delete(Long chatId) {
        Optional<User> userOptional = userRepository.findById(chatId);
        userOptional.ifPresent(user -> userRepository.delete(user));
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
                    "Username: :"+user.getUsername()+"\n+"
                  //  + "Teams: " + user.getTeams()
                    ;
        }
        else return "Your data is clear";
    }



}
