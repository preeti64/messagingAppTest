package com.example.messagingapp.tests;

import com.example.messagingapp.bean.SendMessageRequest;
import com.example.messagingapp.controller.MessageController;
import com.example.messagingapp.dto.MessageDTO;
import com.example.messagingapp.model.Message;
import com.example.messagingapp.model.User;
import com.example.messagingapp.service.MessageServiceImpl;
import com.example.messagingapp.service.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;

import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MessageControllerTests {

    @Mock
    private MessageServiceImpl messageService;

    @Mock
    private UserServiceImpl userService;

    @InjectMocks
    private MessageController messageController;

    @Test
    void testSendMessage() {
        SendMessageRequest request = new SendMessageRequest();
        request.setSenderId(1L);
        request.setReceiverId(2L);
        request.setContent("Hello");

        User sender = new User();
        sender.setId(1L);

        User receiver = new User();
        receiver.setId(2L);

        when(userService.findUserById(1L)).thenReturn(sender);
        when(userService.findUserById(2L)).thenReturn(receiver);

        Message message = new Message();

        when(messageService.sendMessage(sender, receiver, "Hello")).thenReturn(CompletableFuture.completedFuture(message));

        ResponseEntity<?> responseEntity = messageController.sendMessage(request);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    void testGetReceivedMessages() {
        Long userId = 1L;
        User user = new User();
        user.setId(userId);

        Message message = new Message();
        message.setMessageId(1L);
        message.setMessageContent("Hello");

        when(userService.findUserById(userId)).thenReturn(user);
        when(messageService.getReceivedMessages(user)).thenReturn(Collections.singletonList(message));

        ResponseEntity<List<MessageDTO>> responseEntity = messageController.getReceivedMessages(userId);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        List<MessageDTO> messageDTOs = responseEntity.getBody();
        assert messageDTOs != null;
        assertEquals(1, messageDTOs.size());
        MessageDTO messageDTO = messageDTOs.get(0);
        assertEquals(1L, messageDTO.getMessageId());
        assertEquals("Hello", messageDTO.getMessageContent());
    }

    @Test
    void testGetSentMessagesSuccess() {
        Long userId = 1L;
        User user = new User();
        user.setId(userId);

        Message message = new Message();
        message.setMessageId(1L);
        message.setMessageContent("Hello");

        when(userService.findUserById(userId)).thenReturn(user);
        when(messageService.getSentMessages(user)).thenReturn(Collections.singletonList(message));

        ResponseEntity<List<MessageDTO>> responseEntity = messageController.getSentMessages(userId);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        List<MessageDTO> messageDTOs = responseEntity.getBody();
        assert messageDTOs != null;
        assertEquals(1, messageDTOs.size());
        MessageDTO messageDTO = messageDTOs.get(0);
        assertEquals(1L, messageDTO.getMessageId());
        assertEquals("Hello", messageDTO.getMessageContent());
    }

    @Test
    void testGetMessagesFromUserSuccess() {
        Long senderId = 1L;
        Long receiverId = 2L;

        User sender = new User();
        sender.setId(senderId);

        User receiver = new User();
        receiver.setId(receiverId);

        Message message = new Message();
        message.setMessageId(1L);
        message.setMessageContent("Hello");

        when(userService.findUserById(senderId)).thenReturn(sender);
        when(userService.findUserById(receiverId)).thenReturn(receiver);
        when(messageService.getMessagesFromUser(sender, receiver)).thenReturn(Collections.singletonList(message));

        ResponseEntity<List<MessageDTO>> responseEntity = messageController.getMessagesFromUser(senderId, receiverId);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        List<MessageDTO> messageDTOs = responseEntity.getBody();
        assert messageDTOs != null;
        assertEquals(1, messageDTOs.size());
        MessageDTO messageDTO = messageDTOs.get(0);
        assertEquals(1L, messageDTO.getMessageId());
        assertEquals("Hello", messageDTO.getMessageContent());
    }

}
