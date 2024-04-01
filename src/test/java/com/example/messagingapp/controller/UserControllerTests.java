package com.example.messagingapp.controller;

import com.example.messagingapp.model.User;
import com.example.messagingapp.repository.UserRepository;
import com.example.messagingapp.service.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserControllerTests {

    @InjectMocks
    private UserController userController;
    @Mock
    private UserServiceImpl userService;
    @Mock
    private UserRepository userRepository;

    @Test
    public void testCreateUser_Success() {
        String nickName = "testUser";
        User user = new User();
        user.setId(1L);
        user.setNickName(nickName);

        when(userRepository.findByNickName(nickName)).thenReturn(null);
        // Ensure that nickName is in the correct case
        when(userService.createNewUser(nickName)).thenReturn(user);

        ResponseEntity<?> responseEntity = userController.createUser(nickName);
        System.out.println("Response status code: " + responseEntity.getStatusCodeValue());
        System.out.println("Response body: " + responseEntity.getBody());
        System.out.println("Actual Status Code: " + responseEntity.getStatusCodeValue());

        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        User createdUser = (User) responseEntity.getBody();
        assertNotNull(createdUser);
        assertEquals(nickName, createdUser.getNickName());
        assertNotNull(createdUser.getId());

    }

    @Test
    public void testCreateUser_NicknameExists() {
        String nickName = "test1User";
        User existingUser = new User();

        lenient(). when(userRepository.findByNickName(nickName)).thenReturn(existingUser);

        ResponseEntity<?> responseEntity = userController.createUser(nickName);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

}

