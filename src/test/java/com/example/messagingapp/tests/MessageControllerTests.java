package com.example.messagingapp.tests;

import com.example.messagingapp.bean.SendMessageRequest;
import com.example.messagingapp.controller.MessageController;
import com.example.messagingapp.dto.MessageDTO;
import com.example.messagingapp.model.Message;
import com.example.messagingapp.model.User;
import com.example.messagingapp.repository.UserRepository;
import com.example.messagingapp.service.MessageServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MessageControllerTests {

    @Mock
    private MessageServiceImpl messageService;

    @InjectMocks
    private MessageController messageController;

    @Mock
    private UserRepository userRepository;

    @Test
    void testSendMessageValidRequest() {

        User sender = new User();
        sender.setId(1L);

        User receiver = new User();
        receiver.setId(2L);

        when(userRepository.findById(1L)).thenReturn(Optional.of(sender));
        when(userRepository.findById(2L)).thenReturn(Optional.of(receiver));

        when(messageService.sendMessage(sender, receiver, "Hello"))
                .thenThrow(new IllegalArgumentException("Cannot send a message to yourself"));

        SendMessageRequest request = new SendMessageRequest();
        request.setSenderId(1L);
        request.setReceiverId(2L);
        request.setContent("Hello");

        ResponseEntity<?> response = messageController.sendMessage(request);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testSendMessageSelfMessage() {
        User user = new User();
        user.setId(1L);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        SendMessageRequest request = new SendMessageRequest();
        request.setSenderId(1L);
        request.setReceiverId(1L);
        request.setContent("Hello");

        ResponseEntity<?> response = messageController.sendMessage(request);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void testGetReceivedMessagesValidRequest() {
        User user = new User();
        user.setId(1L);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        List<Message> mockMessages = List.of(new Message(), new Message());
        when(messageService.getReceivedMessages(user)).thenReturn(mockMessages);

        ResponseEntity<List<MessageDTO>> response = messageController.getReceivedMessages(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());

         assertEquals(mockMessages.size(), Objects.requireNonNull(response.getBody()).size());
    }

    @Test
    void testGetReceivedMessagesWithMessages() {
        // Mock the UserRepository to return a User
        User receiver = new User();
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(receiver));

        // Mock the MessageService to return true for hasReceivedMessages

        // Mock the received messages
        List<Message> receivedMessages = Arrays.asList(new Message(), new Message());
        when(messageService.getReceivedMessages(receiver)).thenReturn(receivedMessages);

        // Call the controller method
        ResponseEntity<List<MessageDTO>> response = messageController.getReceivedMessages(1L);

        // Verify that the response status is OK (200)
        assertEquals(HttpStatus.OK, response.getStatusCode());
        // Verify that the body contains the correct number of message DTOs
        assertEquals(receivedMessages.size(), Objects.requireNonNull(response.getBody()).size());
    }

    @Test
    void testGetReceivedMessagesNoMessages() {
        // Mock the UserRepository to return a User
        User receiver = new User();
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(receiver));

        // Mock the MessageService to return false for hasReceivedMessages
        //when(messageService.hasReceivedMessages(receiver)).thenReturn(false);

        // Call the controller method
        ResponseEntity<List<MessageDTO>> response = messageController.getReceivedMessages(1L);

        // Verify that the response status is NOT_FOUND (404)
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        // Verify that the body is null
        assertNull(response.getBody());
    }


    @Test
    void testGetSentMessagesValidRequest() {
        User user = new User();
        user.setId(1L);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        List<Message> mockMessages = List.of(new Message(), new Message());
        when(messageService.getSentMessages(user)).thenReturn(mockMessages);
        //when(messageService.hasSentMessages(user)).thenReturn(true);

        ResponseEntity<List<MessageDTO>> response = messageController.getSentMessages(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockMessages.size(), Objects.requireNonNull(response.getBody()).size());
    }
    @Test
    void testGetSentMessagesNoMessages() {
        // Mock the UserRepository to return a User
        User sender = new User();
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(sender));

        // Mock the MessageService to return false for hasReceivedMessages
       // when(messageService.hasSentMessages(sender)).thenReturn(false);

        // Call the controller method
        ResponseEntity<List<MessageDTO>> response = messageController.getSentMessages(1L);

        // Verify that the response status is NOT_FOUND (404)
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        // Verify that the body is null
        assertNull(response.getBody());
    }

    @Test
    void testGetMessagesFromUserValidRequest() {
        User sender = new User();
        sender.setId(1L);
        when(userRepository.findById(1L)).thenReturn(Optional.of(sender));

        User receiver = new User();
        receiver.setId(2L);
        when(userRepository.findById(2L)).thenReturn(Optional.of(receiver));

        List<Message> mockMessages = List.of(new Message(), new Message());
        when(messageService.getMessagesFromUser(sender, receiver)).thenReturn(mockMessages);
        //when(messageService.hasSentMessagesToReceiver(sender, receiver)).thenReturn(true);

        ResponseEntity<List<MessageDTO>> response = messageController.getMessagesFromUser(1L, 2L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockMessages.size(), Objects.requireNonNull(response.getBody()).size());
    }
}

