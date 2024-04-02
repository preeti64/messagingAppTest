package com.example.messagingapp.controller;

import com.example.messagingapp.bean.UserResponse;
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
    public ResponseEntity<?> createUser(@RequestParam (required = false) String nickName) {
        try {
            if (nickName == null || nickName.trim().isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Nickname value is empty. Please add a value.");
            }
            String uniNickName = nickName.toLowerCase();
            UserResponse userResponse = userServiceImpl.createNewUser(uniNickName);
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
