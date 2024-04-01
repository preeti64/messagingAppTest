package com.example.messagingapp.service;

import com.example.messagingapp.model.User;
import com.example.messagingapp.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl{

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User createNewUser(String nickName) {
        String uniNickName = nickName.toLowerCase();
        User existingUser = userRepository.findByNickName(uniNickName);
        if (existingUser != null) {
            throw new IllegalArgumentException("Nickname already exists");
        }
        User user = new User();
        user.setNickName(uniNickName);
        return userRepository.save(user);
    }
}
