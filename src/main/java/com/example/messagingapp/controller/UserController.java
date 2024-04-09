package com.example.messagingapp.controller;

import com.example.messagingapp.controller.model.UserResponse;
import com.example.messagingapp.service.UserServiceImpl;
import com.example.messagingapp.service.model.User;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponses;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserServiceImpl userServiceImpl;

    public UserController(UserServiceImpl userServiceImpl) {
        this.userServiceImpl = userServiceImpl;
    }

    @ApiOperation(value = "Create a new user", notes = "Provide a nickname to create a new user")
    @ApiResponses(value = {
            @io.swagger.annotations.ApiResponse(code = 201, message = "User created successfully"),
            @io.swagger.annotations.ApiResponse(code = 400, message = "Bad request"),
            @io.swagger.annotations.ApiResponse(code = 500, message = "Internal server error")
    })
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public ResponseEntity<UserResponse> createUser(@RequestParam String nickName) {
        try {
            UserResponse userResponse = userServiceImpl.createNewUser(nickName);
            return ResponseEntity.status(userResponse.getHttpStatus()).body(userResponse);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(UserResponse.builder().message("INTERNAL_SERVER_ERROR").success(false).httpStatus(HttpStatus.INTERNAL_SERVER_ERROR).build());
        }
    }

    @ApiResponses(value = {
            @io.swagger.annotations.ApiResponse(code = 200, message = "User found successfully"),
            @io.swagger.annotations.ApiResponse(code = 400, message = "Bad request"),
            @io.swagger.annotations.ApiResponse(code = 500, message = "Internal server error")
    })
    @RequestMapping(value = "/search", method = RequestMethod.GET)
    public ResponseEntity<List<User>> searchUsers(@RequestParam String nickName) {
        try {
            if (nickName == null || nickName.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }
            return ResponseEntity.ok(userServiceImpl.findUsersByNickNameStartingWith(nickName));

        } catch (RuntimeException e) {
            System.err.println("Error while searching users: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();

        }
    }

    @ApiResponses(value = {
            @io.swagger.annotations.ApiResponse(code = 200, message = "All users fetched successfully"),
            @io.swagger.annotations.ApiResponse(code = 500, message = "Internal server error")
    })
    @RequestMapping(value = "/getAllUsers", method = RequestMethod.GET)
    public ResponseEntity<Page<User>> getAllUsers(
            @RequestParam(defaultValue = "0") Integer pageNo,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(defaultValue = "id") String sortBy) {
            try {
                Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));
                Page<User> pageResult = userServiceImpl.getAllUsers(pageable);
                return ResponseEntity.ok(pageResult);
            } catch (RuntimeException e) {
                System.err.println("Error while fetching all users: " + e.getMessage());
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
        }


    }
