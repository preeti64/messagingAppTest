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
        if (nickName.trim().isEmpty()) {
            userResponse.setMessage("Nickname value is empty. Please add a value.");
            userResponse.setStatus(1);
            return userResponse;
        }
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

    public boolean isValidNickname(String nickname) {
        if (nickname == null || nickname.isEmpty()) {
            return false;
        }
        int minLength = 3;
        int maxLength = 30;
        return nickname.length() >= minLength && nickname.length() <= maxLength;
    }

    public boolean isValidUserId(Long userId) {
        return userId != null && userId > 0;
    }
}
