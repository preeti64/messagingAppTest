package com.example.messagingapp.service.tests;

import com.example.messagingapp.bean.UserResponse;
import com.example.messagingapp.model.User;
import com.example.messagingapp.repository.UserRepository;
import com.example.messagingapp.service.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class UserServiceTests {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void testCreateNewUser_Success() {
        String nickname = "testUser";
        String lowerCaseNickname = nickname.toLowerCase();

        when(userRepository.findByNickName(lowerCaseNickname)).thenReturn(null);

        UserResponse userResponse = userService.createNewUser(nickname);

        assertEquals("New user created", userResponse.getMessage());
        assertEquals(0, userResponse.getStatus());

        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void testCreateNewUser_NicknameExists() {
        String nickname = "testUser";
        String lowerCaseNickname = nickname.toLowerCase();

        when(userRepository.findByNickName(lowerCaseNickname)).thenReturn(new User());

        UserResponse userResponse = userService.createNewUser(nickname);

        assertEquals("Nickname already exists", userResponse.getMessage());
        assertEquals(1, userResponse.getStatus());

        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void testCreateNewUser_EmptyNickname() {
        String nickname = "";

        UserResponse userResponse = userService.createNewUser(nickname);

        assertEquals("Nickname value is empty. Please add a value.", userResponse.getMessage());
        assertEquals(1, userResponse.getStatus());

        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void testIsValidNickname() {
        assertTrue(userService.isValidNickname("test"));
        assertFalse(userService.isValidNickname(null));
        assertFalse(userService.isValidNickname(""));
        assertFalse(userService.isValidNickname("te"));
        assertFalse(userService.isValidNickname("testtesttesttesttesttesttesttesttestt"));
    }

    @Test
    void testIfValidUserIdExists() {
        assertTrue(userService.ifValidUserIdExists(null));
        assertTrue(userService.ifValidUserIdExists(0L));
        assertFalse(userService.ifValidUserIdExists(1L));
    }

}

