package com.example.messagingapp.controller;

import com.example.messagingapp.controller.model.UserResponse;
import com.example.messagingapp.service.UserServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserServiceImpl userServiceImpl;

    public UserController(UserServiceImpl userServiceImpl) {
        this.userServiceImpl = userServiceImpl;
    }

    @PostMapping
    public ResponseEntity<?> createUser(@RequestParam String nickName) {
        try {
            UserResponse userResponse = userServiceImpl.createNewUser(nickName);
            if (userResponse.getStatus() == 1) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(userResponse);
            } else {
                return ResponseEntity.status(HttpStatus.CREATED).body(userResponse);
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
