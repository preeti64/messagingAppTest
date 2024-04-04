package com.example.messagingapp.controller.tests;

import com.example.messagingapp.controller.UserController;
import com.example.messagingapp.controller.model.UserResponse;
import com.example.messagingapp.service.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserControllerTests {

    @Mock
    private UserServiceImpl userService;

    @Test
    void testCreateUser() {
        String nickName = "testUser";
        UserResponse userResponse = UserResponse.builder()
                .message("New user created")
                .success(true)
                .httpStatus(HttpStatus.CREATED)
                .build();
        when(userService.createNewUser(nickName)).thenReturn(userResponse);

        UserController userController = new UserController(userService);

        ResponseEntity<UserResponse> responseEntity = userController.createUser(nickName);

        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertEquals(userResponse, responseEntity.getBody());
    }

    @Test
    void testCreateUserNicknameExists() {
        String nickName = "existingUser";
        UserResponse userResponse = UserResponse.builder()
                .message("Nickname already exists")
                .success(false)
                .httpStatus(HttpStatus.BAD_REQUEST)
                .build();
        when(userService.createNewUser(nickName)).thenReturn(userResponse);

        UserController userController = new UserController(userService);

        ResponseEntity<UserResponse> responseEntity = userController.createUser(nickName);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals(userResponse, responseEntity.getBody());
    }
}
