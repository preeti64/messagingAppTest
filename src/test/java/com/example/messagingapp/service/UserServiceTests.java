package com.example.messagingapp.service;

import com.example.messagingapp.model.User;
import com.example.messagingapp.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class UserServiceTests {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateNewUser_Success() {
        String nickname = "many";
        User user = new User();
        user.setId(23L);
        user.setNickName(nickname);
        when(userRepository.findByNickName(nickname)).thenReturn(null);
        when(userService.createNewUser(nickname)).thenReturn(user);

        User newUser = userService.createNewUser(nickname);

        assertEquals(newUser, newUser);
    }

    @Test
    public void testCreateNewUser_NicknameExists() {

        String existingNickname = "existingUser";
        when(userRepository.findByNickName(existingNickname)).thenReturn(new User());
    }
}

