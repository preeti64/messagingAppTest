package com.example.messagingapp.service;

import com.example.messagingapp.controller.model.UserResponse;
import com.example.messagingapp.service.model.User;
import com.example.messagingapp.repository.UserRepository;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserResponse createNewUser(String nickName) {
        UserResponse userResponse = new UserResponse();
        if (nickName == null || nickName.isEmpty()) {
            userResponse.setMessage("Nickname value is empty. Please add a value.");
            userResponse.setHttpStatus(HttpStatus.BAD_REQUEST);
            return userResponse;
        }
        String lowerCaseNickName = nickName.trim().toLowerCase();
        try {
            User existingUser = userRepository.findByNickName(lowerCaseNickName);
            if (existingUser != null) {
                userResponse.setMessage("Nickname already exists");
                userResponse.setHttpStatus(HttpStatus.BAD_REQUEST);
                return userResponse;
            } else {
                User user = new User();
                user.setNickName(lowerCaseNickName);
                userRepository.save(user);
                userResponse.setMessage("New user created");
                userResponse.setHttpStatus(HttpStatus.CREATED);
            }
        } catch (DataIntegrityViolationException e) {
            userResponse.setMessage("Nickname already exists");
            userResponse.setHttpStatus(HttpStatus.BAD_REQUEST);
            throw new IllegalArgumentException("Nickname already exists");

        }

        return userResponse;
    }

    public boolean ifValidUserIdExists(Long userId) {
        return userId == null || userId <= 0;
    }

    public User findUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + userId));
    }

    public List<User> findUsersByNickNameStartingWith(String nickName) {
        try {
            return userRepository.findByNickNameStartingWith(nickName, Sort.by("nickName"));
        } catch (DataAccessException e) {
            throw new RuntimeException("Error while fetching users");

        }
    }


}
