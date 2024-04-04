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
    public ResponseEntity<UserResponse> createUser(@RequestParam String nickName) {
        try {
            UserResponse userResponse = userServiceImpl.createNewUser(nickName);
            return ResponseEntity.status(userResponse.getHttpStatus()).body(userResponse);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(UserResponse.builder().message("INTERNAL_SERVER_ERROR").success(false).httpStatus(HttpStatus.INTERNAL_SERVER_ERROR).build());
        }
    }
}
