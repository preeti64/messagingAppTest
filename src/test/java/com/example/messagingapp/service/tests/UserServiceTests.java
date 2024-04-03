package com.example.messagingapp.service.tests;

import com.example.messagingapp.controller.model.UserResponse;
import com.example.messagingapp.repository.UserRepository;
import com.example.messagingapp.service.UserServiceImpl;
import com.example.messagingapp.service.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.DataIntegrityViolationException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class UserServiceTests {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testCreateNewUserSuccess() {
        String nickName = "testUser";
        when(userRepository.findByNickName(anyString())).thenReturn(null);

        UserResponse response = userService.createNewUser(nickName);

        assertEquals(0, response.getStatus());
        assertEquals("New user created", response.getMessage());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    public void testCreateNewUserNicknameExists() {
        String nickName = "existingUser";
        when(userRepository.findByNickName(anyString())).thenReturn(new User());

        UserResponse response = userService.createNewUser(nickName);

        assertEquals(1, response.getStatus());
        assertEquals("Nickname already exists", response.getMessage());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    public void testCreateNewUserEmptyNickname() {
        String nickName = "";

        UserResponse response = userService.createNewUser(nickName);

        assertEquals(1, response.getStatus());
        assertEquals("Nickname value is empty. Please add a value.", response.getMessage());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    public void testCreateNewUserNullNickname() {
        String nickName = null;

        UserResponse response = userService.createNewUser(nickName);

        assertEquals(1, response.getStatus());
        assertEquals("Nickname value is empty. Please add a value.", response.getMessage());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    public void testCreateNewUserDuplicateNicknameException() {
        String nickName = "testUser";
        when(userRepository.findByNickName(anyString())).thenThrow(DataIntegrityViolationException.class);

        try {
            userService.createNewUser(nickName);
        } catch (RuntimeException e) {
            assertEquals("Nickname already exists", e.getMessage());
        }
    }
}
