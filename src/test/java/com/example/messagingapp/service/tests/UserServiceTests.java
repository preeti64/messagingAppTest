package com.example.messagingapp.service.tests;

import com.example.messagingapp.controller.model.UserResponse;
import com.example.messagingapp.repository.UserRepository;
import com.example.messagingapp.service.UserServiceImpl;
import com.example.messagingapp.service.model.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTests {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;


    @Test
    public void testCreateNewUserSuccess() {
        String nickName = "testuser";
        when(userRepository.findByNickName(nickName)).thenReturn(null);

        UserResponse response = userService.createNewUser(nickName);

        assertNotNull(response);
        assertEquals("New user created", response.getMessage());
        assertEquals(HttpStatus.CREATED, response.getHttpStatus());
    }

    @Test
    public void testCreateNewUserNickNameExists() {
        String nickName = "existinguser";
        when(userRepository.findByNickName(nickName)).thenReturn(new User());

        UserResponse response = userService.createNewUser(nickName);

        assertNotNull(response);
        assertEquals("Nickname already exists", response.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST, response.getHttpStatus());
    }

    @Test
    public void testCreateNewUserEmptyNickName() {
        String nickName = "";
        UserResponse response = userService.createNewUser(nickName);

        assertNotNull(response);
        assertEquals("Nickname value is empty. Please add a value.", response.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST, response.getHttpStatus());
    }

    @Test
    public void testCreateNewUserNullNickName() {
        String nickName = null;
        UserResponse response = userService.createNewUser(nickName);

        assertNotNull(response);
        assertEquals("Nickname value is empty. Please add a value.", response.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST, response.getHttpStatus());
    }

    @Test
    public void testCreateNewUserDataIntegrityViolationException() {
        String nickName = "testser";
        when(userRepository.findByNickName(nickName)).thenThrow(DataIntegrityViolationException.class);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> userService.createNewUser(nickName));
        assertEquals("Nickname already exists", exception.getMessage());
    }
}



