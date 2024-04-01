package com.example.messagingapp.controller;

import com.example.messagingapp.model.User;
//import com.example.messagingapp.service.UserService;
import com.example.messagingapp.service.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserServiceImpl userServiceImpl;

    @PostMapping
    public ResponseEntity<?> createUser(@RequestParam String nickname) {
        try {
            String nickName = nickname.toLowerCase();
            User newUser = userServiceImpl.createNewUser(nickName);
            System.out.println("Response code from Controller: " + ResponseEntity.status(HttpStatus.CREATED).body(newUser));
            return ResponseEntity.status(HttpStatus.CREATED).body(newUser);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

}
