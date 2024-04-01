
package com.example.messagingapp.tests;

import com.example.messagingapp.bean.UserResponse;
import com.example.messagingapp.model.User;
import com.example.messagingapp.repository.UserRepository;
import com.example.messagingapp.service.UserServiceImpl;
import com.example.messagingapp.utils.MessageAppConstraints;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserControllerTests {
    @InjectMocks
    private UserServiceImpl userService;
    @Mock
    private UserRepository userRepository;

    @Test
    public void testCreateUserSuccess() {
        String nickName = "testUser";
        User user = new User();
        user.setId(1L);
        user.setNickName(nickName);

        when(userRepository.findByNickName(Mockito.anyString())).thenReturn(null);

        UserResponse responseEntity = userService.createNewUser(nickName);

        assertEquals(MessageAppConstraints.SUCCESS, responseEntity.getStatus());

    }

    @Test
    public void testCreateUserNicknameExists() {
        String nickName = "test1User";
        User existingUser = new User();
        existingUser.setNickName(nickName);

        lenient(). when(userRepository.findByNickName(Mockito.anyString())).thenReturn(existingUser);

        UserResponse responseEntity = userService.createNewUser(nickName);

        assertEquals(MessageAppConstraints.FAIL, responseEntity.getStatus());
    }

}
