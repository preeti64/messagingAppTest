package com.example.messagingapp.service;

import com.example.messagingapp.bean.UserResponse;
import com.example.messagingapp.model.User;
import com.example.messagingapp.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserResponse createNewUser(String nickName) {
        UserResponse userResponse = new UserResponse();
        String uniNickName = nickName.toLowerCase();
        User existingUser = userRepository.findByNickName(uniNickName);
        if (existingUser != null) {
            userResponse.setMessage("Nickname already exists");
            userResponse.setStatus(1);
        } else {
            User user = new User();
            user.setNickName(uniNickName);
            userRepository.save(user);
            userResponse.setMessage("New user created");
            userResponse.setStatus(0);
        }
        return userResponse;
    }
}
