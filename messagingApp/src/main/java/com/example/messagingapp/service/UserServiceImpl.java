package com.example.messagingapp.service;

import com.example.messagingapp.model.User;
import com.example.messagingapp.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl{

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User createNewUser(String nickname) {
        String nickName = nickname.toLowerCase();
        User existingUser = userRepository.findByNickName(nickName);
        if (existingUser != null) {
            throw new IllegalArgumentException("Nickname already exists");
        }
        User user = new User();
        user.setNickName(nickname);
        return userRepository.save(user);
    }
}
