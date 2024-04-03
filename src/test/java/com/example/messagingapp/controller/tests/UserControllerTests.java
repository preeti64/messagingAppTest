package com.example.messagingapp.controller.tests;

import com.example.messagingapp.controller.UserController;
import com.example.messagingapp.controller.model.UserResponse;
import com.example.messagingapp.service.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserControllerTests {

    @Mock
    private UserServiceImpl userService;

    @InjectMocks
    private UserController userController;


    @Test
    public void testCreateUserSuccess() {
        String nickName = "testUser";
        UserResponse userResponse = new UserResponse();
        userResponse.setStatus(0);
        userResponse.setMessage("New user created");

        when(userService.createNewUser(anyString())).thenReturn(userResponse);

        ResponseEntity<?> responseEntity = userController.createUser(nickName);

        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertEquals(userResponse, responseEntity.getBody());
    }

    @Test
    public void testCreateUserNicknameExists() {
        String nickName = "existingUser";
        UserResponse userResponse = new UserResponse();
        userResponse.setStatus(1);
        userResponse.setMessage("Nickname already exists");

        when(userService.createNewUser(anyString())).thenReturn(userResponse);

        ResponseEntity<?> responseEntity = userController.createUser(nickName);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals(userResponse, responseEntity.getBody());
    }

}
